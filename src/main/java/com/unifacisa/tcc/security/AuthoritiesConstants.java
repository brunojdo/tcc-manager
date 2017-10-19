package com.unifacisa.tcc.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

	//Foi criado constantes que definem o que cada privilegio de usuário representa
	
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String PROFESSOR = "ROLE_PROFESSOR";

    public static final String ALUNO = "ROLE_ALUNO";
    
    public static final String COORDENADOR = "ROLE_COORDENADOR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
