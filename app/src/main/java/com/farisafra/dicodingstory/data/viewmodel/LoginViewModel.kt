import androidx.lifecycle.ViewModel
import com.farisafra.dicodingstory.data.repository.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postLogin(email: String, password: String) = storyRepository.login(email, password)
}