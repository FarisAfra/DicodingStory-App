package com.farisafra.dicodingstory.data.viewmodel

import LoginViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.farisafra.dicodingstory.data.DataDummy
import com.farisafra.dicodingstory.data.getOrAwaitValue
import com.farisafra.dicodingstory.data.repository.StoryRepository
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.farisafra.dicodingstory.data.repository.Result

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private var dummyLoginResponse = DataDummy.dummyLoginSuccess()
    private val dummyEmail = "email@dummy.com"
    private val dummyPassword = "password123"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `Login Success`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLoginResponse)
        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)

    }

    @Test
    fun `Login failed`() {
        dummyLoginResponse = DataDummy.dummyLoginError()

        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Error("invalid password")
        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        val actualResponse = loginViewModel.postLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}