package com.sparrowzoo.coder.domain;

import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.repository.ProjectConfigRepository;
import com.sparrowzoo.coder.repository.TableConfigRepository;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Getter
public class DomainRegistry {
    @Inject
    private TableConfigRepository tableConfigRepository;

    @Inject
    private ProjectConfigRepository projectConfigRepository;
}
