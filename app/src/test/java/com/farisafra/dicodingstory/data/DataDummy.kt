package com.farisafra.dicodingstory.data

import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.response.login.LoginResultResponse
import com.farisafra.dicodingstory.data.response.register.RegisterResponse
import com.farisafra.dicodingstory.data.response.story.AddStoryResponse
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.response.story.StoryResponse

object DataDummy {

    fun dummyRegisterSuccess(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun dummyRegisterError(): RegisterResponse {
        return RegisterResponse(
            error = true,
            message = "bad request"
        )
    }
    fun dummyLoginSuccess(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResultResponse(
                userId = "userId",
                name = "name",
                token = "token"
            )
        )
    }

    fun dummyLoginError(): LoginResponse {
        return LoginResponse(
            error = true,
            message = "invalid password"
        )
    }

    fun dummyAddStorySuccess(): AddStoryResponse {
        return AddStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun dummyAddStoryError(): AddStoryResponse {
        return AddStoryResponse(
            error = true,
            message = "error"
        )
    }

    fun dummyStory(): StoryResponse {
        return StoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                Story(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.01,
                    lon = 0.01
                )
            )
        )
    }
}