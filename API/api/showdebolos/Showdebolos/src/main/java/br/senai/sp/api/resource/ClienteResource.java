package br.senai.sp.api.resource;

import java.util.List;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.api.model.Celular;
import br.senai.sp.api.model.Cliente;
import br.senai.sp.api.model.Confeiteiro;
import br.senai.sp.api.repository.CelularRepository;
import br.senai.sp.api.repository.ClienteRepository;
import br.senai.sp.api.repository.ConfeiteiroRepository;
import br.senai.sp.api.storage.Disco;
import br.senai.sp.api.utils.ConverterData;

@CrossOrigin(origins = "http://showdebolos.tk")
@RestController
@RequestMapping("/cliente")
public class ClienteResource {

	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	CelularRepository celularRepository;
	
	@Autowired
	Disco disco;

	//TRAZ TODOS OS CLIENTES
	@GetMapping
	@CrossOrigin(origins ="http://localhost:3000")
	public List<Cliente> getClientes(){
		return clienteRepository.findAll();
	}
	
	//TRAZ O CLIENTE A PARTIR DO CODIGO
	@GetMapping("{codCliente}")
	@CrossOrigin(origins ="http://localhost:3000")
	public Cliente getCliente(@PathVariable Long codCliente){
		return clienteRepository.findByCod(codCliente);
	}
	
	
	//CADASTRA O CLIENTE
	@PostMapping
	@CrossOrigin(origins = "http://localhost:3000")
	public Cliente gravarCliente(@Validated @RequestBody Cliente cliente){
		
		// Pegando a data de nascimento que foi inserida pelo usuário
		String dtNascimento = cliente.getDtNasc();
		
		// Pegando o email que foi inserido pelo usuário
		String email = cliente.getEmail();
		
		// Pegando o retorno da consulta feita comparando se o email inserido já existe no banco
		int emailValido = clienteRepository.findByEmail(email);
		
		
		// Se o emailValido retornar 0, significa que não existe nenhum cliente que cadastrou aquele email
		if(emailValido == 0) {
			
			// Converte a data inserida no padrão dd/MM/yyyy 
			// para o padrão yyyy-MM-dd que será inserida no banco
			cliente.setDtNasc(ConverterData.dataBanco(dtNascimento));
			
			Celular celular = cliente.getCelular();
			cliente.setStatus(true);
				
			// Salva o cliente cadastrado
			celularRepository.save(celular);
			Cliente clienteSalvo = clienteRepository.save(cliente);
				
			// retorna o cliente que foi cadastrado
			return clienteSalvo;
				
			
		}else {
			
			// caso o emailValido não retorne 0, significa que um email já foi cadastrado
			// então retornará null 
			return null;
		}
		
	
	}
	
	
	@GetMapping("/email/{email}")
	@CrossOrigin(origins = "http://localhost:3000")
	public int verificarEmail(@PathVariable String email) {
		
		int emailValido = clienteRepository.findByEmail(email);
		
		return emailValido;
		
	}
	
	@GetMapping("/cpf/{cpf}")
	@CrossOrigin(origins = "http://localhost:3000")
	public int verificarCPF(@PathVariable String cpf) {
		
		int cpfValido = clienteRepository.findByCPF(cpf);
		
		return cpfValido;
		
	}
	
	
	@PutMapping("/{codCliente}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Cliente> atualizaCliente (@PathVariable Long codCliente, @RequestBody Cliente cliente	){
		Cliente clienteSalvo = clienteRepository.findById(codCliente).get();
		
		BeanUtils.copyProperties(cliente, clienteSalvo,"codCliente");
		
		clienteRepository.save(cliente);
		
		return ResponseEntity.ok(clienteSalvo);
	}
	
	@PutMapping("senha/{codCliente}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Cliente> atualizaSenhaCliente (@PathVariable Long codCliente, @RequestBody Cliente cliente){
		
		
		
		Cliente clienteSalvo = clienteRepository.findById(codCliente).get();
		
		BeanUtils.copyProperties(cliente, clienteSalvo,"codConfeiteiro");
		
		clienteRepository.save(cliente);
		
		return ResponseEntity.ok(clienteSalvo);
	}
	
	
	/*("email/{codCliente}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Cliente> atualizaEmailClienteonfeiteiro (@PathVariable Long codCliente, @RequestBody Cliente cliente){
		
		Cliente clienteSalvo = clienteRepository.findById(codCliente).get();
		
		BeanUtils.copyProperties(cliente, clienteSalvo,"codCliente");
		
		clienteRepository.save(confeiteiro);
		
		return ResponseEntity.ok(confeiteiroSalvo);
	}*/
}
