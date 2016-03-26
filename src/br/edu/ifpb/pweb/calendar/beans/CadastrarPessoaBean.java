package br.edu.ifpb.pweb.calendar.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb.calendar.dao.PersistenceUtil;
import br.edu.ifpb.pweb.calendar.dao.PessoaDAO;
import br.edu.ifpb.pweb.calendar.model.Admin;
import br.edu.ifpb.pweb.calendar.model.Pessoa;
import br.edu.ifpb.pweb.calendar.model.Usuario;


@ManagedBean
@RequestScoped
public class CadastrarPessoaBean extends GenericBean {
	private String nome;
	private String senha;
	private String reSenha;
	
	public String cadastrarPessoa(int tipo){
		
		if(!this.senha.equals(this.reSenha)){
			this.addInfoMessage("Senha diferente, digite novamente!");
			return null;
		}else{
			PessoaDAO pDAO = new PessoaDAO(PersistenceUtil.getCurrentEntityManager());
			Pessoa p = null;
			
			if(tipo == 0){
				p = new Admin();
				p.setName(this.nome);
				p.setSenha(this.senha);
			}else if(tipo == 1){
				p = new Usuario(this.nome, this.senha);				
			}
			
			pDAO.beginTransaction();
			pDAO.insert(p);
			pDAO.commit();
			
			this.addInfoMessage("Cadastro realizado com sucesso!");
			return "index.xhtml?faces-redirect=true";
		}
		
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getReSenha() {
		return reSenha;
	}
	public void setReSenha(String reSenha) {
		this.reSenha = reSenha;
	}
	
}
