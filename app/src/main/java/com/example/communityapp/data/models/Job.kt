package com.example.communityapp.data.models

data class Job(
    val businessName: String,
    val contact: String,
    val jobDescription: String,
    val jobTitle: String,
    val requirements: List<String>,
    val salary: Int
)
