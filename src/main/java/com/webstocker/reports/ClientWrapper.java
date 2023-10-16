
package com.webstocker.reports;

import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Client;
import com.webstocker.domain.Facture;
import com.webstocker.domain.Localite;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Athanase
 */
public final class ClientWrapper {

    private String nomClient;
    private String localiteClient;
    private String telephoneClient;
    private String boitepostale;
    private String categorieclient;
    private Set<Facture> factures = new HashSet<>();
    private String localite;
    private int nbreFacture;
    
    public ClientWrapper(Client client){
        this.nomClient = client.getNomClient();
        this.localiteClient = client.getLocalite()!=null?client.getLocalite().getDisplayName():" ";
        this.boitepostale = client.getBoitepostale();
        this.telephoneClient = client.getTelephoneClient();
      this.categorieclient = client.getCategorieclient()!=null?client.getCategorieclient().getLibelleCategorieclient():" ";
       // this.factures = (Set<Facture>) client.getFactures().iterator().next();   
       //this.nbreFacture = getNbreFacture(client);
    }

    public int getNbreFacture(Client client) {
        
        int nombreFacture = 0;
        Set<Facture> setfacture = null;
              setfacture =  client.getFactures();
        
        Iterator it = setfacture.iterator();
        while (it.hasNext()){
           //Facture facture = (Facture) it.next();
           System.out.print("ESSAI "+it.next());
//           if (Objects.equals(client.getId(), facture.getClient().getId())){
//               nombreFacture ++;
//           }           
        }
        System.out.print("YA PLUS FACTURE ");
        return nbreFacture = nombreFacture;
    }

    public void setNbreFacture(int nbreFacture ) {
              this.nbreFacture = nbreFacture;
    }

    
    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getLocaliteClient() {
        return localiteClient;
    }

    public void setLocaliteClient(String localiteClient) {
        this.localiteClient = localiteClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getBoitepostale() {
        return boitepostale;
    }

    public void setBoitepostale(String boitepostale) {
        this.boitepostale = boitepostale;
    }

    public String getCategorieclient() {
        return categorieclient;
    }

    public void setCategorieclient(String categorieclient) {
        this.categorieclient = categorieclient;
    }

    public Set<Facture> getFactures() {
        return factures;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }
    
    
}
