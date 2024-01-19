CREATE TABLE roles_privileges (role_id INTEGER NOT NULL, privilege_id INTEGER NOT NULL, FOREIGN KEY (role_id) REFERENCES roles(id), FOREIGN KEY (privilege_id) REFERENCES privileges(id));
ALTER TABLE roles_privileges ADD PRIMARY KEY (role_id, privilege_id);
CREATE TABLE users_roles (user_id INTEGER NOT NULL, role_id INTEGER NOT NULL, FOREIGN KEY (role_id) REFERENCES roles(id), FOREIGN KEY (user_id) REFERENCES users(id));
ALTER TABLE users_roles ADD PRIMARY KEY (user_id, role_id);