import com.example.addressbook.AddAppController;
import com.example.addressbook.User;
import com.example.addressbook.UserDAOStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddAppControllerTest {

    private AddAppController controller;
    private User testUser;
    private UserDAOStub userDAOStub;

    @BeforeEach
    void setUp() {
        controller = new AddAppController();
        testUser = new User("TestUser", "TestPassword", "TestEmail");
        userDAOStub = new UserDAOStub(); // Initialize the UserDAOStub
        controller.setUserDAO(userDAOStub); // Set the UserDAO in the controller
    }

    @Test
    void shouldCreateAddAppControllerInstance() {
        assertNotNull(controller, "Controller should not be null");
    }

    @Test
    void shouldSetCurrentUser() {
        controller.getUser(testUser);
        assertEquals(testUser, controller.getCurrentUser(), "Current user should be set correctly");
    }

    @Test
    void shouldSetUserDAO() {
        controller.setUserDAO(userDAOStub);
        assertEquals(userDAOStub, controller.getUserDAO(), "UserDAO should be set correctly");
    }



}
