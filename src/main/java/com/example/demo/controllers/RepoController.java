package com.example.demo.controllers;

import com.example.demo.models.TechnicalReview;
import com.example.demo.services.RepoService;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


@RestController
public class RepoController {

    @Autowired
    public RepoService repoService;



}
