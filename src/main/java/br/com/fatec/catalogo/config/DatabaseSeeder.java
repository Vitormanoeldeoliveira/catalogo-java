package br.com.fatec.catalogo.config;

import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Injeção de dependência do repositório
    public DatabaseSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Verifica se já existe algum usuário com o login 'admin'
        if (usuarioRepository.findByLogin("admin").isEmpty()) {

            // 2. Instancia o usuário padrão
            UsuarioModel admin = new UsuarioModel();
            admin.setLogin("admin");

            // 3. Criptografa a senha padrão 'admin123'
            admin.setSenha(encoder.encode("admin123"));
            admin.setPerfil("ADMIN");

            // 4. Salva no banco de dados
            usuarioRepository.save(admin);

            System.out.println(">>> [DATABASE] Usuário administrador padrão (admin/admin123) criado com sucesso!");
        } else {
            System.out.println(">>> [DATABASE] Usuário 'admin' já existe. Pulando inicialização.");
        }
    }
}