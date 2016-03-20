package com.aclastudios.spaceconquest.PlayGameService;

public interface PlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void submitScore(int highScore);
    public void showScore();
    public boolean isSignedIn();
}