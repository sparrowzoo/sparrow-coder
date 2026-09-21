package com.sparrow.coder.domain;

import com.sparrow.coder.repository.ProjectConfigRepository;
import com.sparrow.coder.repository.TableConfigRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Named
@Getter
@Slf4j
public class CoderDomainRegistry {
    public CoderDomainRegistry() {
        log.info("CoderDomainRegistry init");
    }

    @Inject
    private TableConfigRepository tableConfigRepository;

    @Inject
    private ProjectConfigRepository projectConfigRepository;
}
