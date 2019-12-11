package br.senai.sp.api.resource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.api.model.Celular;
import br.senai.sp.api.model.Cliente;
import br.senai.sp.api.model.Confeiteiro;
import br.senai.sp.api.repository.CelularRepository;
import br.senai.sp.api.repository.ConfeiteiroRepository;
import br.senai.sp.api.utils.ConverterData;

@CrossOrigin(origins = "http://showdebolos.tk")
@RestController
@RequestMapping("/confeiteiro")
public class ConfeiteiroResource {
	
	@Autowired
	public ConfeiteiroRepository confeiteiroRepository;
	
	@Autowired
	CelularRepository celularRepository;
	
	
	@GetMapping
	@CrossOrigin(origins = "http://localHost:3000")
	public List<Confeiteiro> getConfeiteiros(){
		return confeiteiroRepository.findAll();
	}
	
	//RETORNA APENAS UM CONFEITEIRO
	@GetMapping("/{codConfeiteiro}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Confeiteiro getConfeiteiro(@PathVariable Long codConfeiteiro){
		
		Confeiteiro confeiteiro = confeiteiroRepository.findByCod(codConfeiteiro);
		
		return confeiteiro;
	}
		
	//CADASTRA O CONFEITEIRO
	@PostMapping
	@CrossOrigin(origins = "http://localhost:3000")
	public Confeiteiro gravarConfeiteiro(@Validated @RequestBody Confeiteiro confeiteiro){
		
		// Pegando a data de nascimento que foi inserida pelo usuário
				String dtNascimento = confeiteiro.getDtNasc();
				
				// Pegando o email que foi inserido pelo usuário
				String email = confeiteiro.getEmail();
				
				// Pegando o retorno da consulta feita comparando se o email inserido já existe no banco
				int emailValido = confeiteiroRepository.findByEmail(email);
				
				// Se o emailValido retornar 0, significa que não existe nenhum cliente que cadastrou aquele email
				if(emailValido == 0) {
					
					// Converte a data inserida no padrão dd/MM/yyyy 
					// para o padrão yyyy-MM-dd que será inserida no banco
					confeiteiro.setDtNasc(ConverterData.dataBanco(dtNascimento));
					
					confeiteiro.setStatus(1);
					
					Celular celular = confeiteiro.getCelular();
						
					// Salva o cliente cadastrado
					celularRepository.save(celular);
					Confeiteiro confeiteiroSalvo = confeiteiroRepository.save(confeiteiro);
						
					// retorna o cliente que foi cadastrado
					return confeiteiroSalvo;
						
					
				}else {
					
					// caso o emailValido não retorne 0, significa que um email já foi cadastrado
					// então retornará null 
					return null;
				}
		
	}
	
	//VERIFICA SE O EMAIL DO CONFEITEIRO EXISTE NO BANCO
	@GetMapping("/email/{email}")
	@CrossOrigin(origins = "http://localhost:3000")
	public int verificarEmail(@PathVariable String email) {
		
		int emailValido = confeiteiroRepository.findByEmail(email);
		
		return emailValido;
		
	}
	
	@GetMapping("/verifica/atualizacao/{email}")
	public int verificaAtualizaçãoEmail(@PathVariable String email, @RequestBody String senha) {
		
		int valido = confeiteiroRepository.findConfeiteiroByEmailAndSenha2(email, senha);
		
		return valido;
	}
	
	//VERIFICA SE O CPF DO CONFEITEIRO EXISTE NO BANCO
	@GetMapping("/cpf/{cpf}")
	@CrossOrigin(origins = "http://localhost:3000")
	public int verificarCPF(@PathVariable String cpf) {
		
		int cpfValido = confeiteiroRepository.findByCPF(cpf);
		
		return cpfValido;
		
	}
	
	
	//VERIFIVA SE A SENHA DO CONFEITEIRO ESTÁ CORRETA AO TENTAR ALTERAR
	@GetMapping("/senha/{codConfeiteiro}/{senha}")
	@CrossOrigin(origins = "http://localhost:3000")
	public int verificaSenha(@PathVariable Long codConfeiteiro, @PathVariable String senha) {
		
		int confeiteiro = confeiteiroRepository.findByConfeiteiroSenha(codConfeiteiro, senha);
		
		System.out.println(codConfeiteiro);
		System.out.println(senha);
		
		
		return confeiteiro;
		
	}
	
	//atualiza os dados do confeiteiro sem cpf, senha, email
	/*@PutMapping("/{codConfeiteiro}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Confeiteiro> atualizaConteiteiro (@PathVariable Long codConfeiteiro, @RequestBody Confeiteiro confeiteiro	){
		Confeiteiro confeiteiroSalvo = confeiteiroRepository.findById(codConfeiteiro).get();
		
		BeanUtils.copyProperties(confeiteiro, confeiteiroSalvo,"codConfeiteiro");
		
		confeiteiroRepository.save(confeiteiro);
		
		return ResponseEntity.ok(confeiteiro);
	}*/
	
	//atualiza os dados do confeiteiro sem cpf, senha, email
//	@PatchMapping("/{codConfeiteiro}")
//	@CrossOrigin(origins = "http://localhost:3000")
//	public ResponseEntity<Confeiteiro> atualizaConteiteiro (@PathVariable Long codConfeiteiro, @RequestBody Map<String, Object>	confeiteiro){
//		
//		Confeiteiro confeiteiroSalvo = confeiteiroRepository.findByCod(codConfeiteiro);
//		
//		
//		
//		confeiteiro.remove("codConfeiteiro");
//		
//		confeiteiro.forEach((k, v) -> {
//			
//			Field confeiteiroParcial = ReflectionUtils.findField(Confeiteiro.class, k);
//			confeiteiroParcial.setAccessible(true);
//			
//			System.out.println("OBJETO" + confeiteiroParcial);
//			
//			ReflectionUtils.setField(confeiteiroParcial, confeiteiroSalvo, v );
//		});
//
//		
//
//		
//		return ResponseEntity.ok(confeiteiroRepository.save(confeiteiroSalvo));
//		
//		
//		
//		System.out.println(confeiteiro);
//		//pega o objeto celular
//		Celular celular = confeiteiroSalvo.getCelular();
//		
//		// Converte a data inserida no padrão dd/MM/yyyy 
//		// para o padrão yyyy-MM-dd que será inserida no banco
//		confeiteiro.setDtNasc(ConverterData.dataBanco(confeiteiro.getDtNasc()));
//		
//		
//		//seta nesse celular o celular que estou passando no corpo
//		celular.setCelular(confeiteiro.getCelular().getCelular());
//		
//		//depois passa o retorno do objeto no confeiteiro
//		confeiteiro.setCelular(celular);
//		
//		confeiteiro.setCpf(confeiteiroSalvo.getCpf());
//		confeiteiro.setEmail(confeiteiroSalvo.getEmail());
//		confeiteiro.setSenha(confeiteiroSalvo.getSenha());
//		confeiteiro.setStatus(confeiteiroSalvo.getStatus());
//		confeiteiro.setAvaliacao(confeiteiroSalvo.getAvaliacao());
//		
//		BeanUtils.copyProperties(confeiteiro, confeiteiroSalvo,"codConfeiteiro");
//		
//		confeiteiroRepository.save(confeiteiro);
//		
//		return ResponseEntity.ok(confeiteiro);
	//}
	
	
	//ATUALIZA A SENHA DO CONFEITEIRO
	@PutMapping("/senha/{codConfeiteiro}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Confeiteiro> atualizaSenhaConfeiteiro (@PathVariable Long codConfeiteiro, @RequestBody String senha){
		
		Confeiteiro confeiteiroSalvo = confeiteiroRepository.findById(codConfeiteiro).get();
	
		confeiteiroSalvo.setSenha(senha);
		
		confeiteiroRepository.save(confeiteiroSalvo);
		
		return ResponseEntity.ok(confeiteiroSalvo);
	}
	
	
	//ATUALIZA O EMAIL DO CONFEITEIRO
	@PutMapping("/email/{codConfeiteiro}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Confeiteiro> atualizaEmailConfeiteiro (@PathVariable Long codConfeiteiro, @RequestBody String email){
		
		Confeiteiro confeiteiroSalvo = confeiteiroRepository.findById(codConfeiteiro).get();
		
		
		confeiteiroSalvo.setEmail(email);
		//BeanUtils.copyProperties(confeiteiro, confeiteiroSalvo,"codConfeiteiro");
		
		confeiteiroRepository.save(confeiteiroSalvo);
		
		return ResponseEntity.ok(confeiteiroSalvo);
	}
	
	//TRAZ O EMAIL DO CONFEITEIRO PARA MOSTRAR NA APLICAÇÃO
	@GetMapping("/pegar/{codConfeiteiro}")
	@CrossOrigin(origins = "http://localhost:3000")
	public String pegarEmailConfeiteiro(@PathVariable Long codConfeiteiro) {
		
		Confeiteiro confeiteiro = confeiteiroRepository.findByCod(codConfeiteiro);
		
		return confeiteiro.getEmail();
	}
	
	

}
