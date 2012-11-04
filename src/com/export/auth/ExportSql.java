package com.export.auth;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mld.auth.MldLog;
import com.mld.auth.ProprieteCleEtrangere;

public class ExportSql {
	private String m_dataBase;
	private MldLog m_mld;
	private String m_sql;
	
	public ExportSql(String dataBase, McdGraph mcd) {
		m_dataBase = dataBase;
		m_mld = new MldLog(mcd);
		m_sql = "";
		
		createSQL();
	}
	
	public void save(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter(){
			public boolean accept(File arg0) {
				if(arg0.isDirectory())
					return true;
				String ext = getExtension(arg0);
				if(ext==null)
					return false;
				if(ext.equals("sql"))
					return true;
				return false;
			}

			public String getDescription() {
				return "SQL Only";
			}
			
		});
		if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(getExtension(file)==null||!getExtension(file).equals("sql"))
				file = new File(file.getAbsolutePath()+".sql");
				try {
					FileWriter writer = new FileWriter(file);
					BufferedWriter output = new BufferedWriter(writer);
					output.write(m_sql);
					output.flush();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	public String getSql(){
		return m_sql;
	}
	private void createSQL(){
		m_sql += createDataBase(m_dataBase);
		
		for (Entite e : m_mld.getEntites()){
			if (!containsCleEtrangere(e))
				m_sql += createTable(e);
		}
		
		for (Entite e : m_mld.getEntites()){
			if (containsCleEtrangere(e)){
				m_sql += createTable(e);
				m_sql += ajoutCleEtrangere(e, getCleEtrangere(e));
			}
		}
	}
	
	private String ajoutCleEtrangere(Entite e, List<ProprieteCleEtrangere> fk){
		String sql = "";
		String cle = "";
		
		for (ProprieteCleEtrangere p : fk){
			for (Propriete id : p.getEntite().getProprietes())
				if (id.isClePrimaire())
					cle = id.getVirtualName(p.getEntite().getName());
			
			sql += "ALTER TABLE "+e.getName()+"\n";
			sql += "\tADD CONSTRAINT FK_"+e.getName().toUpperCase()+"_"+p.getEntite().getName().toUpperCase()+" FOREIGN KEY("+p.getVirtualName(e.getName())+") REFERENCES "+p.getEntite().getName()+"("+cle+");\n";
		}
		return sql;
	}
	
	private String createDataBase(String s){
		String sql = "";
		
		sql += "CREATE DATABASE \""+s+"\";\n";
		return sql;
	}
	
	private String createTable(Entite e){
		String sql = "";
		Propriete clePrimaire = null;
		
		sql += "CREATE TABLE "+e.getName()+"(\n";
		for (Propriete p : e.getProprietes()){
			sql += "\t"+p.getVirtualName(e.getName())+" "+p.getType().getName();
			if(p.getType().getNombreTaille()!=0){
				boolean def = true;
				for(int i=0;i<p.getType().getNombreTaille();++i){
					if(p.getTaille(i)!=0)
						def=false;
				}
				if(!def){
					boolean first=true;
					sql+="(";
					for(int i=0;i<p.getType().getNombreTaille();++i){
						if(!first)
							sql+=",";
						sql+=p.getTaille(i);
					}
					sql+=")";
				}
			}
			
			if (p.isNull())
				sql += " NULL";
			else
				sql += " NOT NULL";
			
			if (p.isAutoIncrement())
				sql += " IDENTITY(1, 1)";
			
			if (p.isClePrimaire())
				clePrimaire = p;
			
			if (!e.getProprietes().get(e.getProprietes().size()-1).equals(p))
				sql += ",\n";
		}
		
		if (clePrimaire != null)
			sql += ",\n\tCONSTRAINT PK_"+e.getName().toUpperCase()+" PRIMARY KEY("+clePrimaire.getVirtualName(e.getName())+")";
		
		sql += "\n);\n";
		
		return sql;
	}
	
	private boolean containsCleEtrangere(Entite e){
		for (Propriete p : e.getProprietes()){
			if (p instanceof ProprieteCleEtrangere){
				return true;
			}
		}
		return false;
	}
	
	private List<ProprieteCleEtrangere> getCleEtrangere(Entite e){
		List<ProprieteCleEtrangere> fk = new ArrayList<ProprieteCleEtrangere>();
		for (Propriete p : e.getProprietes()){
			if (p instanceof ProprieteCleEtrangere){
				fk.add((ProprieteCleEtrangere)p);
			}
		}
		return fk;
	}
	
	private String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}

}
