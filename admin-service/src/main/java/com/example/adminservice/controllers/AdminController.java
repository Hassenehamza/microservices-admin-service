package com.example.adminservice.controllers;




import com.example.adminservice.AdminService;
import com.example.adminservice.dto.Agent;
import com.example.adminservice.dto.Client;
import com.example.adminservice.dto.Transfer;
import com.example.adminservice.entities.Admin;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class AdminController
{
    private final AdminService  adminService;
    private RestTemplate restTemplate;

    @Autowired
    public AdminController(AdminService adminService)
    {
        this.adminService = adminService;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping(value = "/admins")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Admin> getAdmins()
    {
        System.out.println("getting admins");
        return this.adminService.getAdmins();
    }

    @GetMapping(path = "/agents")

    public List<Agent> getAgents()
    {
        System.out.println("getAgents has been called");
        return (List<Agent>) restTemplate.exchange("http://agent-service/get-agents", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Agent>>() {});
    }

    @PostMapping(value = "/add-agent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addAgent(@RequestBody Agent agent)
    {
        restTemplate.postForObject(
                "http://agent-service/add-agent",
                agent,
                Agent.class);
        System.out.println("Agent added successfully");
    }

    @PostMapping(value = "/add-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addAdmin(@RequestBody Admin admin)
    {
        adminService.addAdmin(admin);
    }
    @PostMapping(value = "/add-client")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addClient(@RequestBody Client client)
    {
        restTemplate.postForObject("http://agent-service/add-agent",client,Client.class);
        System.out.println("client added successfully");
    }

    @PostMapping(value = "/block-transfer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void blockTransfer(@RequestBody String transferReference)
    {
        restTemplate.postForObject("http://transfert-service/bloquer/" + transferReference,null,null);
        System.out.println("client added successfully");
    }

    @PostMapping(value = "/unblock-transfer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void unblockTransfer(@RequestBody String transferReference)
    {
        restTemplate.postForObject("http://transfert-service/debloquer/"+transferReference,null,null);
        System.out.println("client added successfully");
    }

/*    @GetMapping(path = "/block-transfer")
    @PreAuthorize("hasAnyAuthority('admin:read','agent:read')")
    public Transfer blockTransfer()
    {
        restTemplate.postForObject("http://transfert-service/bloquer-transfer", null, );
    }*/



}