package de.oth.sw.entity.converter;

import de.oth.sw.entities.Ort;
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
public class OrtConverter implements Converter, Serializable {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String ortId) {
        return em.find(Ort.class, Long.parseLong(ortId));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        if (!(entity instanceof Ort)) {
            throw new IllegalArgumentException(entity.getClass().getName() + " passed to OrtConverter");
        }

        long id = ((Ort) entity).getId();
        return id + "";
    }

}
