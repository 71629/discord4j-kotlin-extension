package com.hashtag071629.event.slash.options

import discord4j.discordjson.json.ApplicationCommandOptionChoiceData

public fun choice(name: String, value: Any): ApplicationCommandOptionChoiceData =
    ApplicationCommandOptionChoiceData.builder().name(name).value(value).build()