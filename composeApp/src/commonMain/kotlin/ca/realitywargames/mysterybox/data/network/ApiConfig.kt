package ca.realitywargames.mysterybox.data.network

expect val baseUrl: String

// API base URL derived from base URL
val apiBaseUrl: String = "$baseUrl/api/v1"

// Base URL for the server (same as baseUrl for images and other resources)
val serverBaseUrl: String = baseUrl


