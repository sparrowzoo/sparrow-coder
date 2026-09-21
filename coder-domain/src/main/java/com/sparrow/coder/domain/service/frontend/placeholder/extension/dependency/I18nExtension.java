package com.sparrow.coder.domain.service.frontend.placeholder.extension.dependency;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

@Named
public class I18nExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        tableContext.getPlaceHolder().put(PlaceholderKey.$frontend_i18n_message.name(), JsonFactory.getProvider().toString(tableContext.getI18nMap()));
    }
}
