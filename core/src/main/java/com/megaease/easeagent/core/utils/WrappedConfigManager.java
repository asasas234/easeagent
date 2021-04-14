package com.megaease.easeagent.core.utils;

import com.megaease.easeagent.config.ConfigManagerMXBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WrappedConfigManager implements ConfigManagerMXBean {
    private final ClassLoader customClassLoader;
    private final ConfigManagerMXBean conf;

    public WrappedConfigManager(ClassLoader customClassLoader, ConfigManagerMXBean config) {
        this.customClassLoader = customClassLoader;
        this.conf = config;
    }

    @Override
    public void updateConfigs(Map<String, String> configs) {
        ThreadUtils.callWithClassLoader(customClassLoader, () -> {
            conf.updateConfigs(configs);
            return null;
        });
    }

    @Override
    public void updateService(String json, String version) throws IOException {
        try {
            ThreadUtils.callWithClassLoader(customClassLoader, () -> {
                try {
                    conf.updateService(json, version);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    @Override
    public void updateCanary(String json, String version) throws IOException {
        try {
            ThreadUtils.callWithClassLoader(customClassLoader, () -> {
                try {
                    conf.updateCanary(json, version);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    @Override
    public void updateService2(Map<String, String> configs, String version) {
        ThreadUtils.callWithClassLoader(customClassLoader, () -> {
            conf.updateService2(configs, version);
            return null;
        });
    }

    @Override
    public void updateCanary2(Map<String, String> configs, String version) {
        ThreadUtils.callWithClassLoader(customClassLoader, () -> {
            conf.updateCanary2(configs, version);
            return null;
        });
    }

    @Override
    public Map<String, String> getConfigs() {
        return ThreadUtils.callWithClassLoader(customClassLoader, conf::getConfigs);
    }

    @Override
    public List<String> availableConfigNames() {
        return ThreadUtils.callWithClassLoader(customClassLoader, conf::availableConfigNames);
    }
}
