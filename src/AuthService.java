public class AuthService {
    private String currentUsername;
    private AI currentAI;
    private boolean loggedIn;
    
    public AuthService(){
        currentUsername = "";
        currentAI = new AI();
        loggedIn  = false;
    }

    public AuthService(String currentUsername, AI currentAI, boolean loggedIn){
        this.currentUsername = currentUsername;
        this.currentAI = currentAI;
        this.loggedIn = loggedIn;
    }

    public String getCurrentUsername(){
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername){
        this.currentUsername = currentUsername;
    }

    public AI getCurrentAI() {
        return currentAI;
    }

    public void setCurrentAI(AI currentAI){
        this.currentAI = currentAI;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

}
