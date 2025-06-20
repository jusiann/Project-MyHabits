package com.adil.ProjectMyHabits;

public class UserSession {
	private String username;
    private String name;
    private String surname;
    private String password;
	private String salt;
    private Character gender;
    private String age;
    private String height;
    private String weight;
    private String avatarPath;
    private boolean isFirstLogin;
    private static UserSession currentSession;

    public UserSession(String username, String name, String surname, String password, String salt) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
		this.salt = salt;
        this.age = "";
        this.height = "";
        this.weight = "";
        this.gender = ' ';
        this.avatarPath = "";
        this.isFirstLogin = true;
    }

    
    public UserSession(String username, 
    		String name, String surname, 
    		String password, String salt, Character gender, String age,
    		String height, String weight, 
    		String avatarPath, Boolean isFirstLogin) {
		super();
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.salt = salt;
		this.gender = gender;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.avatarPath = avatarPath;
		this.isFirstLogin = isFirstLogin;
	}


	public static void setSession(String username, String name, String surname, String password, String salt) {
        currentSession = new UserSession(username, name, surname, password, salt);
    }
	
    public static UserSession getSession() { 
        return currentSession;
    }

	public static void setSession(String username, 
			String name, String surname, 
			String password, String salt, Character gender, String age,
			String height, String weight, 
			String avatarPath, Boolean isFirstLogin) {
	        currentSession = new UserSession(username, name, surname, password, salt, gender, age, height, weight, avatarPath, isFirstLogin);
	    }
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() { return salt; }

	public void setSalt(String salt) { this.salt = salt; }

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
}
