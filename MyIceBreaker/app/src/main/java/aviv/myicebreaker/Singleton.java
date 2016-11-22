package aviv.myicebreaker;

import aviv.myicebreaker.module.CustomObjects.NewUser;

/**
 * Created by Aviv on 03/07/2016.
 */


    public class Singleton {
        private static Singleton instance = null;
    private NewUser newUser;

        private Singleton() {

        }

    public static Singleton getInstance(){
        if(instance == null)
        {
            instance = new Singleton();
        }
        return instance;
    }
        // other useful methods here

    public NewUser getNewUser() {
        return newUser;
    }

    public void setNewUser(NewUser newUser) {
        this.newUser = newUser;
    }
}
