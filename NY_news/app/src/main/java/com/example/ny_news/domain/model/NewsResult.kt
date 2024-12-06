package com.example.ny_news.domain.model

data class NewsResult(val id: String,
                      val subsection: String,
                      val title: String,
                      val abstract: String,
                      val url: String,
                      val byline: String,
                      val published_date: String,
                      val multimedia: List<Multimedia>)
