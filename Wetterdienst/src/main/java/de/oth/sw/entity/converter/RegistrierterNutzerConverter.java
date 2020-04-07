package de.oth.sw.entity.converter;

import de.oth.sw.entities.RegistrierterNutzer;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Maximilian Fries <maximilian.fries@st.oth-regensburg.de>
 */
@Dependent
public class RegistrierterNutzerConverter implements Converter, Serializable {

    @PersistenceContext
    private EntityManager em;

    public RegistrierterNutzerConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String RegistrierterNutzerId) {
        return em.find(RegistrierterNutzer.class, Long.parseLong(RegistrierterNutzerId));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        if (!(entity instanceof RegistrierterNutzer)) {
            throw new IllegalArgumentException(entity.getClass().getName() + " passed to OrtConverter");
        }

        long id = ((RegistrierterNutzer) entity).getId();
        return id + "";
    }

}
