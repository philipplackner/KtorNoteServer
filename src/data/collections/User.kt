package com.androiddevs.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email: String,
    val password: String,
    @BsonId
    val id: String = ObjectId().toString()
)