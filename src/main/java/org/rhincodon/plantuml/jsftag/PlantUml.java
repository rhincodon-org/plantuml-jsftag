/*
 * ==============================================================================
 * Rhincodon Org.
 * Copyright(C) 2017
 * All Blue Ocean Co., Ltd.
 * All rights reserved.
 * ==============================================================================
 */
package org.rhincodon.plantuml.jsftag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * plant uml.
 *
 * @author rhincodon.org
 */
@FacesComponent(value = "plantuml", tagName = "plantuml", createTag = true)
public class PlantUml extends UIComponentBase {

    /**
     * start uml.
     */
    private static final String START_UML = "@startuml";

    /**
     * end uml.
     */
    private static final String END_UML = "@enduml";

    /**
     * start span.
     */
    private static final String START_SPAN = "<span>";

    /**
     * end span.
     */
    private static final String END_SPAN = "</span>";

    /**
     * start img for base64.
     */
    private static final String START_IMG_FOR_BASE64 = "<img src=\"data:image/png;base64,";

    /**
     * end img for base64.
     */
    private static final String END_IMG_FOR_BASE64 = "\"/>";

    /**
     * get family.
     *
     * @return string.
     */
    @Override
    public String getFamily() {
        return this.getClass().getPackage().getName();
    }

    /**
     * encode begin.
     *
     * @param context
     * @throws IOException
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        // reading input string.
        StringBuilder inputStr = new StringBuilder();
        inputStr.append(START_UML);
        inputStr.append(System.lineSeparator());
        for (UIComponent uIComponent : this.getChildren()) {
            inputStr.append(uIComponent);
            inputStr.append(System.lineSeparator());
        }
        inputStr.append(END_UML);
        this.getChildren().clear();

        // generate png image in memory.
        SourceStringReader reader = new SourceStringReader(inputStr.toString());
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        reader.generateImage(pngOutputStream);

        // encoding base64.
        byte[] pngBytes = pngOutputStream.toByteArray();
        String pngBase64Str = Base64.getEncoder().encodeToString(pngBytes);

        // write response.
        ResponseWriter rw = context.getResponseWriter();
        rw.write(START_SPAN);
        rw.write(START_IMG_FOR_BASE64 + pngBase64Str + END_IMG_FOR_BASE64);
    }

    /**
     * encode end.
     *
     * @param context
     * @throws IOException
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter rw = context.getResponseWriter();
        rw.write(END_SPAN);
    }

}
