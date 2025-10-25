package com.apiIc.api.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiIc.api.Repositories.UsuarioRepository;
import com.apiIc.api.Repositories.PerfilRepository;
import com.apiIc.api.Repositories.EnderecoRepository;
import com.apiIc.api.Repositories.DeficienciaRepository;
import com.apiIc.api.Repositories.BeneficioRepository;
import com.apiIc.api.Repositories.ProfissaoRepository;
import com.apiIc.api.dto.UsuarioDTO;
import com.apiIc.api.dto.UsuarioFullDTO;
import com.apiIc.api.dto.EnderecoDTO;
import com.apiIc.api.dto.BeneficioDTO;
import com.apiIc.api.dto.ProfissaoDTO;
import com.apiIc.api.entities.Beneficio;
import com.apiIc.api.entities.Deficiencia;
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.entities.Perfil;
import com.apiIc.api.entities.Profissao;
import com.apiIc.api.entities.Usuario;
import com.apiIc.api.entities.enums.PerfilNome;
import com.apiIc.api.entities.enums.Cor;
import com.apiIc.api.entities.enums.Escolaridade;
import com.apiIc.api.entities.enums.Renda_mensal;
import com.apiIc.api.entities.enums.Sexo;
import com.apiIc.api.entities.enums.Tipo_Moradia;
import com.apiIc.api.entities.enums.Estado_civil;
import com.apiIc.api.services.execptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 
 * @author Douglas
 *
 */

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
    
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;
    private final EnderecoRepository enderecoRepository;
    private final DeficienciaRepository deficienciaRepository;
    private final BeneficioRepository beneficioRepository;
    private final ProfissaoRepository profissaoRepository;
    private final GoogleMapsService googleMapsService;
    
    public List<Usuario> findAll(){
        return repository.findAll();
                
    }
    
    public Usuario findByiD(Long id) {
        Optional<Usuario> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        return usuario;
    }

    public Usuario authenticate(String email, String password) {
        Usuario usuario = (Usuario) loadUserByUsername(email);
        
        if (!passwordEncoder.matches(password, usuario.getSenha())) {
            throw new UsernameNotFoundException("Senha inválida");
        }
        
        return usuario;
    }
    
    @Transactional
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
    
    @Transactional
    public Usuario insert(UsuarioDTO objDTO) {
        Usuario obj = new Usuario(); 
        BeanUtils.copyProperties(objDTO, obj, "senha");

        // Criptografa a senha
        obj.setSenha(passwordEncoder.encode(objDTO.getSenha()));
        
        // Salva o usuário primeiro para obter o ID
        Usuario savedUsuario = repository.save(obj);
        
        // Adiciona o perfil padrão ROLE_USER
        Perfil perfil = perfilRepository.findByNome(PerfilNome.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Perfil padrão não encontrado"));
        savedUsuario.addPerfil(perfil);
        
        // Atualiza o usuário com o perfil
        savedUsuario = repository.save(savedUsuario);


    if (savedUsuario.getEnd() != null && !savedUsuario.getEnd().isEmpty()) {
        Set<Endereco> enderecos = new HashSet<>(savedUsuario.getEnd());
        savedUsuario.getEnd().clear();
        for (Endereco endereco : enderecos) {
            savedUsuario.adicionarEndereco(endereco);
        }
    }

    if (savedUsuario.getDefs() != null && !savedUsuario.getDefs().isEmpty()) {
        Set<Deficiencia> deficiencias = new HashSet<>(savedUsuario.getDefs());
        savedUsuario.getDefs().clear();
        for (Deficiencia deficiencia : deficiencias) {
            deficiencia.adicionarUsuario(savedUsuario);
        }
    }

    if (savedUsuario.getBens() != null && !savedUsuario.getBens().isEmpty()) {
        Set<Beneficio> beneficios = new HashSet<>(savedUsuario.getBens());
        savedUsuario.getBens().clear();
        for (Beneficio beneficio : beneficios) {
            beneficio.adicionarUsuario(savedUsuario);
        }
    }

    if (savedUsuario.getProfs() != null && !savedUsuario.getProfs().isEmpty()) {
        Set<Profissao> profissoes = new HashSet<>(savedUsuario.getProfs());
        savedUsuario.getProfs().clear();
        for (Profissao profissao : profissoes) {
            profissao.adicionarUsuario(savedUsuario);
        }
    }

    return repository.save(savedUsuario);
}

    @Transactional
    public Usuario insertFull(UsuarioFullDTO dto) {
        Usuario obj = new Usuario();
        // Campos básicos
        obj.setNome(dto.getNome());
        obj.setNome_social(dto.getNome_social());
        obj.setCpf(dto.getCpf()); // já deve vir formatado como 000.000.000-00
        obj.setEmail(dto.getEmail());
        obj.setTelefone(dto.getTelefone() != null ? dto.getTelefone() : 0L);
        obj.setTelefone_contato(dto.getTelefone_contato() != null ? dto.getTelefone_contato() : 0L);
        obj.setLatitude(dto.getLatitude());
        obj.setLongitude(dto.getLongitude());
        obj.setPlusCode(dto.getPlusCode());

        // Parse data de nascimento dd/MM/yyyy
        if (dto.getData_nascimento() != null && !dto.getData_nascimento().isBlank()) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dn = LocalDate.parse(dto.getData_nascimento(), fmt);
                obj.setData_nascimento(dn);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data de nascimento inválida. Use dd/MM/yyyy");
            }
        }

        // Enums com fallback do socioEconomicScreen, se necessário
        String corIn = dto.getCor();
        String escIn = dto.getEscolaridade();
        String rendaIn = dto.getRenda_mensal();
        String sexoIn = dto.getSexo();
        String moradiaIn = dto.getTipo_moradia();
        String estadoCivilIn = dto.getEstado_civil();

        if (dto.getSocioEconomicScreen() != null) {
            var se = dto.getSocioEconomicScreen();
            if (corIn == null) corIn = se.getCor();
            if (escIn == null) escIn = se.getEscolaridade();
            if (rendaIn == null) rendaIn = se.getRenda_mensal();
            if (sexoIn == null) sexoIn = se.getSexo();
            if (moradiaIn == null) moradiaIn = se.getTipo_moradia();
            if (estadoCivilIn == null) estadoCivilIn = se.getEstado_civil();
        }

        if (corIn != null) obj.setCor(Cor.valueOf(corIn));
        if (escIn != null) obj.setEscolaridade(Escolaridade.valueOf(escIn));
        if (rendaIn != null) obj.setRenda_mensal(Renda_mensal.valueOf(rendaIn));
        if (sexoIn != null) {
            Sexo sx = Sexo.parse(sexoIn);
            if (sx == null) {
                throw new IllegalArgumentException("Sexo inválido: " + sexoIn);
            }
            obj.setSexo(sx);
        }
        if (moradiaIn != null) obj.setTipo_moradia(Tipo_Moradia.valueOf(moradiaIn));
        if (estadoCivilIn != null) obj.setEstado_civil(Estado_civil.valueOf(estadoCivilIn));

        // Criptografa a senha
        obj.setSenha(passwordEncoder.encode(dto.getSenha()));

        // Salva para obter ID
        Usuario savedUsuario = repository.save(obj);

        // Perfil padrão
        Perfil perfil = perfilRepository.findByNome(PerfilNome.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Perfil padrão não encontrado"));
        savedUsuario.addPerfil(perfil);
        savedUsuario = repository.save(savedUsuario);

        // Endereços embutidos (usa id existente ou cria novo)
        if (dto.getEnderecos() != null) {
            for (EnderecoDTO e : dto.getEnderecos()) {
                Endereco end;
                if (e.getId_endereco() != null) {
                    end = enderecoRepository.findById(e.getId_endereco())
                        .orElseThrow(() -> new ResourceNotFoundException(e.getId_endereco()));
                } else {
                    end = new Endereco();
                    end.setTipo_endereco(e.getTipo_endereco());
                    end.setLogradouro(e.getLogradouro());
                    end.setBairro(e.getBairro());
                    end.setCidade(e.getCidade());
                    end.setEstado(e.getEstado());
                    end.setCep(e.getCep());
                    end.setNumero(e.getNumero());

                    // Geocodifica o endereço antes de salvar
                    String enderecoCompleto = String.format("%s, %s, %s - %s, %s",
                            safe(e.getLogradouro()), safe(e.getNumero() != null ? e.getNumero() : ""),
                            safe(e.getCidade()), safe(e.getEstado()), safe(e.getCep()));
                    try {
                        var loc = googleMapsService.getCoordinatesFromAddress(enderecoCompleto);
                        end.setLatitude(loc.getLatitude());
                        end.setLongitude(loc.getLongitude());
                    } catch (Exception ex) {
                        // Regra: em falha, mantém null para lat/long e segue, ou lance exceção conforme regra de negócio
                        end.setLatitude(null);
                        end.setLongitude(null);
                    }
                }
                // relacionamento bidirecional via helper
                savedUsuario.adicionarEndereco(end);
            }
        }

        // Associações: Deficiências (IDs), Benefícios (objetos), Profissões (objetos)
        // Deficiências: aceita top-level ou dentro de socioEconomicScreen
        List<Long> defIds = dto.getDeficienciaIds();
        if ((defIds == null || defIds.isEmpty()) && dto.getSocioEconomicScreen() != null) {
            defIds = dto.getSocioEconomicScreen().getDeficienciaIds();
        }
        if (defIds != null && !defIds.isEmpty()) {
            List<Deficiencia> defs = deficienciaRepository.findAllById(defIds);
            savedUsuario.getDefs().addAll(defs);
        }
        if (dto.getBens() != null && !dto.getBens().isEmpty()) {
            for (BeneficioDTO b : dto.getBens()) {
                Beneficio beneficio;
                if (b.getId_beneficio() != null) {
                    beneficio = beneficioRepository.findById(b.getId_beneficio())
                        .orElseThrow(() -> new ResourceNotFoundException(b.getId_beneficio()));
                    // opcional: atualizar descrição se vier
                    if (b.getDesc_beneficio() != null) beneficio.setDesc_beneficio(b.getDesc_beneficio());
                } else {
                    beneficio = new Beneficio();
                    beneficio.setDesc_beneficio(b.getDesc_beneficio());
                }
                savedUsuario.getBens().add(beneficio);
            }
        }
        if (dto.getProfs() != null && !dto.getProfs().isEmpty()) {
            Set<Profissao> setProfs = new HashSet<>();
            for (ProfissaoDTO p : dto.getProfs()) {
                Profissao prof;
                if (p.getId_profissao() != null) {
                    prof = profissaoRepository.findById(p.getId_profissao())
                        .orElseThrow(() -> new ResourceNotFoundException(p.getId_profissao()));
                    if (p.getDesc_profissao() != null) prof.setDesc_profissao(p.getDesc_profissao());
                } else {
                    prof = new Profissao();
                    prof.setDesc_profissao(p.getDesc_profissao());
                }
                setProfs.add(prof);
            }
            savedUsuario.setProfs(setProfs);
        }

        // Reforça vínculos bidirecionais conforme já realizado no insert()
        if (savedUsuario.getEnd() != null && !savedUsuario.getEnd().isEmpty()) {
            Set<Endereco> enderecos = new HashSet<>(savedUsuario.getEnd());
            savedUsuario.getEnd().clear();
            for (Endereco endereco : enderecos) {
                savedUsuario.adicionarEndereco(endereco);
            }
        }
        if (savedUsuario.getDefs() != null && !savedUsuario.getDefs().isEmpty()) {
            Set<Deficiencia> deficiencias = new HashSet<>(savedUsuario.getDefs());
            savedUsuario.getDefs().clear();
            for (Deficiencia deficiencia : deficiencias) {
                deficiencia.adicionarUsuario(savedUsuario);
            }
        }
        if (savedUsuario.getBens() != null && !savedUsuario.getBens().isEmpty()) {
            Set<Beneficio> beneficios = new HashSet<>(savedUsuario.getBens());
            savedUsuario.getBens().clear();
            for (Beneficio beneficio : beneficios) {
                beneficio.adicionarUsuario(savedUsuario);
            }
        }
        if (savedUsuario.getProfs() != null && !savedUsuario.getProfs().isEmpty()) {
            Set<Profissao> profissoes = new HashSet<>(savedUsuario.getProfs());
            savedUsuario.getProfs().clear();
            for (Profissao profissao : profissoes) {
                profissao.adicionarUsuario(savedUsuario);
            }
        }

        return repository.save(savedUsuario);
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    public Usuario update(Long id, UsuarioDTO objDTO) {
        Usuario entity = repository.getReferenceById(id);
        updateData(entity,objDTO);
        return repository.save(entity);
    }

    private void updateData(Usuario entity, UsuarioDTO objDTO) {
        
        entity.setNome(objDTO.getNome());
        
    }

}
