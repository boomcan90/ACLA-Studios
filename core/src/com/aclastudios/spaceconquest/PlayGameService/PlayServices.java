package com.aclastudios.spaceconquest.PlayGameService;

public interface PlayServices {
    public boolean getSignedInGPGS();

    public void loginGPGS();

    public void logoutGPGS();

    public void submitScoreGPGS(int score);

    public void unlockAchievementGPGS(String achievementId);

    public void getLeaderboardGPGS();

    public void getAchievementsGPGS();

    public void startQuickGame();

    public void seeInvitations();

    public void sendInvitations();

    public void leaveRoom();

}
