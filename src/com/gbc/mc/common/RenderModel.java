/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import hapax.Template;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import org.apache.log4j.Logger;
/**
 *
 * @author tamvh
 */
public class RenderModel {
	private static final Logger log = Logger.getLogger(RenderModel.class);

    public static Template getCTemplate(String tpl) throws Exception {
        TemplateLoader templateLoader = TemplateResourceLoader.create("com/gbc/gateway/email_template/");
        Template template = templateLoader.getTemplate(tpl);
        return template;
    }

}
