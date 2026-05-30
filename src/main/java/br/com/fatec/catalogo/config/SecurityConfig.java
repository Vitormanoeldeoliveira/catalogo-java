package br.com.fatec.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permite que todos vejam o login e arquivos estáticos (CSS/JS)
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()

                        // Gerenciamento de Usuários: SÓ ADMIN pode listar, carregar formulário, editar ou deletar
                        .requestMatchers("/usuarios", "/usuarios/**").hasRole("ADMIN")

                        // CORREÇÃO AQUI: Ajustado para a rota real da sua Controller (/auditoria)
                        // Se você quer que APENAS ADMIN veja o histórico, deixe hasRole("ADMIN")
                        // Se ALUNO também puder ver, mude para .hasAnyRole("ADMIN", "ALUNO")
                        .requestMatchers("/auditoria", "/auditoria/**").hasRole("ADMIN")

                        // Visualização: Aluno e Admin podem ver as listas de produtos e categorias
                        .requestMatchers("/produtos", "/categorias").hasAnyRole("ADMIN", "ALUNO")

                        // Alteração: SÓ ADMIN pode acessar qualquer rota que salve, edite ou delete produtos/categorias
                        .requestMatchers("/produtos/novo/**", "/produtos/editar/**", "/produtos/deletar/**", "/produtos/excluir/**").hasRole("ADMIN")
                        .requestMatchers("/categorias/novo/**", "/categorias/editar/**", "/categorias/deletar/**").hasRole("ADMIN")

                        // Qualquer outra rota não especificada exige apenas estar logado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // O "true" força o redirecionamento para o Dashboard
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Desabilitado para facilitar o teste local, mas cuidado em produção

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}