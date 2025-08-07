package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension.dependency;

import com.sparrow.core.spi.JsonFactory;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;

@Named
public class I18nExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        tableContext.getPlaceHolder().put(PlaceholderKey.$frontend_i18n_message.name(), JsonFactory.getProvider().toString(tableContext.getI18nMap()));
    }
}
