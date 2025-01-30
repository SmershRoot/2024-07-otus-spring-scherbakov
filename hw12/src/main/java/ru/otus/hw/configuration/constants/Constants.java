package ru.otus.hw.configuration.constants;

public class Constants {

    public static class Authority {

        public static final String USER = "USER";

        public static final String ROLE_USER = "ROLE_USER";

        public static final String PREAUTHORIZE_USER = "hasRole('" + USER + "')";


        public static final String ADMIN = "ADMIN";

        public static final String ROLE_ADMIN = "ROLE_ADMIN";

        public static final String PREAUTHORIZE_ADMIN = "hasRole('" + ADMIN + "')";
    }


}
