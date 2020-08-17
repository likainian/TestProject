package com.example.testproject

class MainContract {
    interface MainView {
        fun showHomeData(response: String)
    }

    interface MainPresenter {
        fun getHomeData()
    }
}
