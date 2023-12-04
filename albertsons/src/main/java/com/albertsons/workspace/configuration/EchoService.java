package com.albertsons.workspace.configuration;

import org.springframework.stereotype.Service;

@Service("echo")
public class EchoService {
    public String echo(String msg) {
        return msg;
    }
}
