package br.edu.ifpb.pweb.calendar.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import br.edu.ifpb.pweb.calendar.dao.ComentarioDAO;
import br.edu.ifpb.pweb.calendar.dao.PersistenceUtil;
import br.edu.ifpb.pweb.calendar.dao.PessoaDAO;
import br.edu.ifpb.pweb.calendar.model.CalendarComment;
import br.edu.ifpb.pweb.calendar.model.DiasCalendar;
import br.edu.ifpb.pweb.calendar.model.Usuario;

@ManagedBean
@ViewScoped
public class MagicCalendarBean {
	private final int CREATE = 0;
	private final int UPDATE = 1;
	
	private String texto;
	private Date dataInicio;
	private Date dataFim;
	
	private List<DiasCalendar> diasCalendar;
	private Date dataAtual;
	private String dataAtualConcatenada;
	@ManagedProperty(value="#{pessoaBean}")
	private PessoaBean pessoaBean;
	
	private String tituloPagina;
	private String valueBotao;
	private int opcAltComentario;
	
	private DiasCalendar diaSelecionado;
	private CalendarComment comentarioSelecionado;
	
	public MagicCalendarBean(){
		this.diasCalendar = new ArrayList<DiasCalendar>();
		this.dataAtual = new Date();
	}
	
	public void gerarCalendar(){
		this.addDataAtual(0);
		
		for(int i=1; i<=quantidadeDiaMes(this.dataAtual); i++){
			DiasCalendar data = new DiasCalendar();
			data.setId(i);

			if(this.pessoaBean.getLogado() != null){
				List<CalendarComment> tmpCm = ((Usuario)this.pessoaBean.getLogado()).getListCommentMonth(this.dataAtual);
				if(tmpCm.size() > 0){
					for (CalendarComment cm : tmpCm) {
						if(cm.getStartDate().getDate() == data.getId())
							data.addCalendar(cm);
					}
				}
			}
			
			this.diasCalendar.add(data);
		}
	}
	
	public void deletarComentario(DiasCalendar dia, CalendarComment cm){
		//this.diasCalendar.get(dia.getId()-1).deleteCalendar(cm);
		((Usuario)this.pessoaBean.getLogado()).delComment(cm);
		ComentarioDAO cDAO = new ComentarioDAO(PersistenceUtil.getCurrentEntityManager());
		cm.setUsuario(null);
		cDAO.beginTransaction();
		cDAO.delete(cm);
		cDAO.commit();
	}
	
	public int quantidadeDiaMes(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public void addDataAtual(int i){
		Calendar c = Calendar.getInstance();
		this.dataAtual.setMonth(dataAtual.getMonth()+i);
		c.setTime(this.dataAtual);
		this.dataAtualConcatenada = mesAno(c.get(Calendar.MONTH))+"/"+c.get(Calendar.YEAR);
		this.diasCalendar = new ArrayList<DiasCalendar>();
	}
	
	public String diaSemana(int dia){
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		d.setDate(dia);
		d.setMonth(this.dataAtual.getMonth());
		d.setYear(this.dataAtual.getYear());
		c.setTime(d);
		dia = c.get(Calendar.DAY_OF_WEEK);
		String[] diaSemana = new String[]{"","Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado"}; 
		return diaSemana[dia];
	}
	
	public  String mesAno(int mes){
		String[] mesAno = new String[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto"
									   ,"Setembro","Outubro","Novembro","Dezembro"}; 
		return mesAno[mes];
	}
	
	public String alterarComentario(){
		ComentarioDAO cDAO = new ComentarioDAO(PersistenceUtil.getCurrentEntityManager());
		if(this.opcAltComentario == this.CREATE){
			CalendarComment cm = new CalendarComment();
			cm.setText(this.texto);
			cm.setStartDate(this.addDateSelected());
			cm.setUsuario((Usuario)this.pessoaBean.getLogado());
			cDAO.beginTransaction();
			cDAO.insert(cm);
			cDAO.commit();
			((Usuario)this.pessoaBean.getLogado()).addComment(cm);
		}else if(this.opcAltComentario == this.UPDATE){
			PessoaDAO pDAO = new PessoaDAO(PersistenceUtil.getCurrentEntityManager());
			((Usuario)this.pessoaBean.getLogado()).setComment(this.comentarioSelecionado);
			pDAO.beginTransaction();
			pDAO.update(this.pessoaBean.getLogado());
			pDAO.commit();
			
		}
		return "index.xhtml?faces-redirect=true";
	}
	
	public Date addDateSelected(){
		Date d = new Date();
		d.setDate(this.diaSelecionado.getId());
		d.setMonth(this.dataAtual.getMonth());
		d.setYear(this.dataAtual.getYear());
		return d;
	}
	
	public String setOpcAltComentario(int opc, DiasCalendar dia, CalendarComment cm){
		System.out.println(dia.getId());
		this.opcAltComentario = opc;
		if(opc == this.CREATE){
			this.tituloPagina = "Cadastrar Comentario";
			this.valueBotao = "Cadastrar";
		}else if(opc == this.UPDATE){
			this.tituloPagina = "Atualizar Comentario";
			this.valueBotao = "Atualizar";
			this.diaSelecionado = dia;
			this.comentarioSelecionado = cm;
		}
		loadFlash();
		return "altComentario.xhtml?faces-redirect=true";
	}
	
	public void loadFlash(){
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("dia", this.diaSelecionado);
		flash.put("comentario", this.comentarioSelecionado);
		flash.put("tituloPagina", this.tituloPagina);
		flash.put("valueBotao", this.valueBotao);
	}
	
	public void unloadFlash(){
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		if(flash != null){
			this.setDiaSelecionado((DiasCalendar) flash.get("dia"));
			this.setComentarioSelecionado((CalendarComment) flash.get("comentario"));
			this.setTituloPagina((String) flash.get("tituloPagina"));
			this.setValueBotao((String) flash.get("valueBotao"));
			
			//this.setTexto(this.comentarioSelecionado.getText());
		}
	}
	
	public List<DiasCalendar> getDiasCalendar() {
		return diasCalendar;
	}

	public void setDiasCalendar(List<DiasCalendar> diasCalendar) {
		this.diasCalendar = diasCalendar;
	}

	public String getDataAtualConcatenada() {
		return dataAtualConcatenada;
	}

	public void setDataAtualConcatenada(String dataAtualConcatenada) {
		this.dataAtualConcatenada = dataAtualConcatenada;
	}

	public PessoaBean getPessoaBean() {
		return pessoaBean;
	}

	public void setPessoaBean(PessoaBean pessoaBean) {
		this.pessoaBean = pessoaBean;
	}

	public String getTituloPagina() {
		return tituloPagina;
	}

	public void setTituloPagina(String tituloPagina) {
		this.tituloPagina = tituloPagina;
	}

	public String getValueBotao() {
		return valueBotao;
	}

	public void setValueBotao(String valueBotao) {
		this.valueBotao = valueBotao;
	}

	public int getCREATE() {
		return CREATE;
	}

	public int getUPDATE() {
		return UPDATE;
	}

	public DiasCalendar getDiaSelecionado() {
		return diaSelecionado;
	}

	public void setDiaSelecionado(DiasCalendar diaSelecionado) {
		this.diaSelecionado = diaSelecionado;
	}

	public int getOpcAltComentario() {
		return opcAltComentario;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public CalendarComment getComentarioSelecionado() {
		return comentarioSelecionado;
	}

	public void setComentarioSelecionado(CalendarComment comentarioSelecionado) {
		this.comentarioSelecionado = comentarioSelecionado;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
