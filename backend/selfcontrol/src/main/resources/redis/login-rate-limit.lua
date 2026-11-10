-- login-rate-limit.lua
--
-- KEYS[1] : 更新するRedisキー
--
-- ARGV[1] : 操作名
--           IP_CONSUME      IPの試行回数を増やして判定
--           ACCOUNT_CHECK   アカウントが制限中か確認
--           ACCOUNT_FAILURE アカウントの失敗回数を増やして判定
--           RESET_ACCOUNT   アカウントの状態を削除
--
-- ARGV[2] : 現在時刻（Unix時刻のミリ秒）
-- ARGV[3] : 制限の閾値
-- ARGV[4] : 試行回数を数える時間枠（ミリ秒）
-- ARGV[5] : 制限解除後の段階リセットまでの時間（ミリ秒）
-- ARGV[6] : 制限時間の種類数
-- ARGV[7]以降 : 制限時間（ミリ秒）。短い段階から順に渡す

local key = KEYS[1]
local operation = ARGV[1]

-- ログイン成功時のアカウント状態リセット
if operation == "RESET_ACCOUNT" then
    redis.call("DEL", key)
    return { "RESET", "0" }
end

if operation ~= "IP_CONSUME"
    and operation ~= "ACCOUNT_CHECK"
    and operation ~= "ACCOUNT_FAILURE" then
        return redis.error_reply("Unknown rate limit operation")
end

local now = tonumber(ARGV[2])
local threshold = tonumber(ARGV[3])
local windowMs = tonumber(ARGV[4])
local quietPeriodMs = tonumber(ARGV[5])
local durationCount = tonumber(ARGV[6])

if not now
    or not threshold
    or not windowMs
    or not quietPeriodMs
    or not durationCount
    or durationCount < 1 then
        return redis.error_reply("Invalid rate limit arguments")
end

for index = 1, durationCount do
    if not tonumber(ARGV[6 + index]) then
        return redis.error_reply("Missing block duration")
    end
end

-- Hashから数値の項目を読み込む
local function getNumber(field, defaultValue)
    local value = redis.call("HGET", key, field)

    if not value then
        return defaultValue
    end

    return tonumber(value) or defaultValue
end

local count = getNumber("count", 0)
local stage = getNumber("stage", 0)
local windowStartedAt = getNumber("windowStartedAt", 0)
local blockedUntil = getNumber("blockedUntil", 0)
local stageResetAt = getNumber("stageResetAt", 0)

if stage < 0 then 
    stage = 0
end

if stage > durationCount - 1 then
    stage = durationCount - 1
end

-- 制限後の段階リセット期限を過ぎていたら、
-- 以前の状態を削除して初回状態から始める
if stageResetAt > 0 and now >= stageResetAt then
    redis.call("DEL", key)

    count = 0
    stage = 0
    windowStartedAt = 0
    blockedUntil = 0
    stageResetAt = 0
end

-- 状態のないアカウントは、キーを作らず許可する
if operation == "ACCOUNT_CHECK"
    and redis.call("EXISTS", key) == 0 then
        return { "ALLOW", "0" }
end


-- 制限中なら、制限終了時刻を返す
if blockedUntil > now then
    return { "BLOCKED", tostring(blockedUntil) }
end

-- 制限時間が過ぎていれば、その古い値だけを削除する
if blockedUntil > 0 then
    redis.call("HDEL", key, "blockedUntil")
end

--アカウント確認で、制限中でないことがわかれば終了
if operation == "ACCOUNT_CHECK" then
    return { "ALLOW", "0" }
end

-- 1回目の試行、または時間枠が終わっていたら、
-- 新しい時間枠を開始する
if windowStartedAt == 0 or now >= windowStartedAt + windowMs then
    count = 0
    windowStartedAt = now
end

count = count + 1

-- IPはリミットを超えた時、アカウントは失敗回数がリミットに達したときに制限する
local shouldBlock = false

if operation == "IP_CONSUME" then
    shouldBlock = count > threshold
else
    shouldBlock = count >= threshold
end

if shouldBlock then
    local durationIndex = stage + 1

    if durationIndex > durationCount then
        durationIndex = durationCount
    end

    local blockDurationMs = tonumber(ARGV[6 + durationIndex])
    local newBlockedUntil = now + blockDurationMs
    local newStage = math.min(stage + 1, durationCount - 1)
    local newStageResetAt = newBlockedUntil + quietPeriodMs

    redis.call(
        "HSET",
        key,
        "count", "0",
        "stage", tostring(newStage),
        "blockedUntil", tostring(newBlockedUntil),
        "stageResetAt", tostring(newStageResetAt)
    )

    redis.call("HDEL", key, "windowStartedAt")

    redis.call("PEXPIREAT", key, tostring(newStageResetAt))

    return { "BLOCKED", tostring(newBlockedUntil) }
end

-- 制限に達していない状態をHashに保存する
redis.call(
    "HSET",
    key,
    "count", tostring(count),
    "stage", tostring(stage),
    "windowStartedAt", tostring(windowStartedAt)
)

if stageResetAt > 0 then
    -- 過去に制限されている場合、次の段階を維持するため、
    -- 段階リセット期限までキーを残す
    redis.call("HSET", key, "stageResetAt", tostring(stageResetAt))
    redis.call("PEXPIREAT", key, tostring(stageResetAt))
else
    -- まだ制限されていない場合、現在の時間枠の終了時にキーを消す
    redis.call("HDEL", key, "stageResetAt")
    redis.call(
        "PEXPIREAT",
        key,
        tostring(windowStartedAt + windowMs)
    )
end

return { "ALLOW", "0" }