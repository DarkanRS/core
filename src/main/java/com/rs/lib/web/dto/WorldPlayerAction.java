package com.rs.lib.web.dto;

import com.rs.lib.game.WorldInfo;
import com.rs.lib.model.Account;

public record WorldPlayerAction(Account account, WorldInfo world) { }
