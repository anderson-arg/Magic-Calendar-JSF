package br.edu.ifpb.pweb.calendar.beans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import br.edu.ifpb.pweb.calendar.dao.ComentarioDAO;
import br.edu.ifpb.pweb.calendar.dao.FeriadoDAO;
import br.edu.ifpb.pweb.calendar.dao.FeriadoFixoDAO;
import br.edu.ifpb.pweb.calendar.dao.PersistenceUtil;
import br.edu.ifpb.pweb.calendar.dao.PessoaDAO;
import br.edu.ifpb.pweb.calendar.model.Admin;
import br.edu.ifpb.pweb.calendar.model.Calendar;
import br.edu.ifpb.pweb.calendar.model.CalendarComment;
import br.edu.ifpb.pweb.calendar.model.CalendarFixedHoliday;
import br.edu.ifpb.pweb.calendar.model.DiasCalendar;
import br.edu.ifpb.pweb.calendar.model.Usuario;
import br.edu.ifpb.pweb.calendar.util.Color;

@ManagedBean
public class CadastroCalendarBean extends GenericBean{
	private final int CREATE = 0;
	private final int UPDATE = 1;
	
	private String texto;
	private Date dataInicio;
	private Date dataFim;
	
	private int selectIdFeriado;
	private List<CalendarFixedHoliday> feriadosFixo;
	
	@ManagedProperty(value="#{pessoaBean}")
	private PessoaBean pessoaBean;
	
	public void deletarComentario(DiasCalendar dia, CalendarComment cm){
		//this.diasCalendar.get(dia.getId()-1).deleteCalendar(cm);
		((Usuario)this.pessoaBean.getLogado()).delComment(cm);
		ComentarioDAO cDAO = new ComentarioDAO(PersistenceUtil.getCurrentEntityManager());
		cm.setUsuario(null);
		cDAO.beginTransaction();
		cDAO.delete(cm);
		cDAO.commit();
	}
	
	public String addComentario(){
		ComentarioDAO cDAO = new ComentarioDAO(PersistenceUtil.getCurrentEntityManager());
		CalendarComment cm = new CalendarComment();
		cm.setText(this.texto);
		cm.setStartDate(this.addDateSelected());
		cm.setUsuario((Usuario)this.pessoaBean.getLogado());
		cDAO.beginTransaction();
		cDAO.insert(cm);
		cDAO.commit();
		((Usuario)this.pessoaBean.getLogado()).addComment(cm);

		return "index.xhtml?faces-redirect=true";
	}
	
	public String altComentario(){
		PessoaDAO pDAO = new PessoaDAO(PersistenceUtil.getCurrentEntityManager());
		((Usuario)this.pessoaBean.getLogado()).setComment((CalendarComment)this.pessoaBean.getCalendarSelecionado());
		pDAO.beginTransaction();
		pDAO.update(this.pessoaBean.getLogado());
		pDAO.commit();

		return "index.xhtml?faces-redirect=true";
	}
	
	public String setOpcAltComentario(int opc, DiasCalendar dia, CalendarComment cm, Date dataAtual){
		if(opc == this.CREATE){
			this.pessoaBean.setDiaSelecionado(dia);
			this.pessoaBean.setDataAtual(dataAtual);
			return "addComentario.xhtml?faces-redirect=true";
		}else if(opc == this.UPDATE){
			this.pessoaBean.setDataAtual(dataAtual);
			this.pessoaBean.setDiaSelecionado(dia);
			this.pessoaBean.setCalendarSelecionado(cm);
			return "altComentario.xhtml?faces-redirect=true";
		}
		return null;
	}
	
	public String setOpcAltFeriado(DiasCalendar dia, Date dataAtual){
		this.pessoaBean.setDiaSelecionado(dia);
		this.pessoaBean.setDataAtual(dataAtual);
		return "altFeriado.xhtml?faces-redirect=true";
	}
	
	public void deletarFeriado(Calendar feriado){
		FeriadoDAO fDAO = new FeriadoDAO(PersistenceUtil.getCurrentEntityManager());
		fDAO.beginTransaction();
		fDAO.delete(feriado);
		fDAO.commit();
	}
	
	public String altFeriado(){
		FeriadoDAO pDAO = new FeriadoDAO(PersistenceUtil.getCurrentEntityManager());
		pDAO.beginTransaction();
		pDAO.update(this.pessoaBean.getDiaSelecionado().getFeriado());
		pDAO.commit();
		this.addInfoMessage("Feriado atualizado com sucesso!");
		return "index.xhtml";
	}
	
	public String addFeriadoFixo(){
		FeriadoFixoDAO ffDAO = new FeriadoFixoDAO(PersistenceUtil.getCurrentEntityManager());
		CalendarFixedHoliday cf = new CalendarFixedHoliday();
		cf.setText(this.texto);
		cf.setStartDate(this.dataInicio);
		cf.setColor(Color.RED);
		cf.setAdmin((Admin) this.pessoaBean.getLogado());
		
		ffDAO.beginTransaction();
		ffDAO.insert(cf);
		ffDAO.commit();
		((Admin)this.pessoaBean.getLogado()).setFeriadoFixo(cf);
		
		this.addInfoMessage("Feriado adicionado com sucesso!");
		return "index.xhtml";
	}
	
	// esta travando aqui!!!
	public String addFeriadoSubstituto(){
		FeriadoFixoDAO ffDAO = new FeriadoFixoDAO(PersistenceUtil.getCurrentEntityManager());
		this.pessoaBean.getCalendarSelecionado().setColor(Color.BLUE);
		this.pessoaBean.getCalendarSelecionado().setStartDate(this.dataInicio);
		((CalendarFixedHoliday)this.pessoaBean.getCalendarSelecionado()).setSubstituto((CalendarFixedHoliday)this.pessoaBean.getCalendarSelecionado());
		ffDAO.beginTransaction();
		ffDAO.update((CalendarFixedHoliday)this.pessoaBean.getCalendarSelecionado());
		ffDAO.commit();
		((Admin)this.pessoaBean.getLogado()).setFeriadoFixo((CalendarFixedHoliday)this.pessoaBean.getCalendarSelecionado());
		
		this.addInfoMessage("Feriado substituido com sucesso!");
		return "index.xhtml";
	}
	
	public void carregarSelect(){
		this.feriadosFixo = ((Admin)this.pessoaBean.getLogado()).getListFixedHoliday();
	}
	
	public Date addDateSelected(){
		Date d = new Date();
		d.setDate(this.pessoaBean.getDiaSelecionado().getId());
		d.setMonth(this.pessoaBean.getDataAtual().getMonth());
		d.setYear(this.pessoaBean.getDataAtual().getYear());
		return d;
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

	public PessoaBean getPessoaBean() {
		return pessoaBean;
	}

	public void setPessoaBean(PessoaBean pessoaBean) {
		this.pessoaBean = pessoaBean;
	}

	public int getCREATE() {
		return CREATE;
	}

	public int getUPDATE() {
		return UPDATE;
	}

	public List<CalendarFixedHoliday> getFeriadosFixo() {
		return feriadosFixo;
	}

	public void setFeriadosFixo(List<CalendarFixedHoliday> feriadosFixo) {
		this.feriadosFixo = feriadosFixo;
	}

	public int getSelectIdFeriado() {
		return selectIdFeriado;
	}

	public void setSelectIdFeriado(int selectIdFeriado) {
		this.selectIdFeriado = selectIdFeriado;
	}
	
}
