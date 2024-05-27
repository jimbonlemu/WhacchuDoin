package com.jimbonlemu.whacchudoin

import com.jimbonlemu.whacchudoin.data.network.dto.Story

object FakeData {
    fun autoGenerateFakeData(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..69) {
            val story = Story(
                id = i.toString(),
                name = "Story $i",
                description = "Description $i",
                photoUrl = "https://picsum.photos/200/300?random=$i",
                createdAt = "2021-01-01T00:00:00Z",
                lat = (-7.69),
                lon = 114.69
            )
            items.add(story)
        }
        return items
    }
}