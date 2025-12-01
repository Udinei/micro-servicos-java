# Comandos Úteis do Docker

## Docker Compose - Gerenciamento de Containers

### Iniciar serviços
```bash
# Iniciar em modo detached (background)
docker compose -f icompras-servicos/database/postgres-docker-compose.yml up -d

# Iniciar e ver logs em tempo real
docker compose -f icompras-servicos/database/postgres-docker-compose.yml up
```

### Parar serviços
```bash
# Parar containers (dados preservados)
docker compose -f icompras-servicos/database/postgres-docker-compose.yml stop

# Parar e remover containers (volumes preservados)
docker compose -f icompras-servicos/database/postgres-docker-compose.yml down

# Parar, remover containers E volumes (APAGA TODOS OS DADOS)
docker compose -f icompras-servicos/database/postgres-docker-compose.yml down -v
```

### Reiniciar serviços
```bash
# Reiniciar containers
docker compose -f icompras-servicos/database/postgres-docker-compose.yml restart

# Iniciar containers parados
docker compose -f icompras-servicos/database/postgres-docker-compose.yml start
```

### Ver status e logs
```bash
# Ver status dos containers
docker compose -f icompras-servicos/database/postgres-docker-compose.yml ps

# Ver logs
docker compose -f icompras-servicos/database/postgres-docker-compose.yml logs

# Ver logs em tempo real
docker compose -f icompras-servicos/database/postgres-docker-compose.yml logs -f

# Ver últimas 50 linhas de log
docker compose -f icompras-servicos/database/postgres-docker-compose.yml logs --tail 50
```

## Docker - Comandos Gerais

### Gerenciamento de Containers

```bash
# Listar containers em execução
docker ps

# Listar todos os containers (incluindo parados)
docker ps -a

# Parar um container específico
docker stop <container_id_ou_nome>

# Parar todos os containers em execução
docker stop $(docker ps -q)

# Remover um container
docker rm <container_id_ou_nome>

# Remover um container em execução (forçar)
docker rm -f <container_id_ou_nome>

# Remover todos os containers parados
docker container prune
```

### Gerenciamento de Imagens

```bash
# Listar imagens
docker images

# Remover uma imagem
docker rmi <image_id_ou_nome>

# Remover imagens não utilizadas
docker image prune

# Remover TODAS as imagens não utilizadas
docker image prune -a

# Remover todas as imagens (forçar)
docker rmi $(docker images -q) -f
```

### Gerenciamento de Volumes

```bash
# Listar volumes
docker volume ls

# Remover um volume
docker volume rm <volume_name>

# Remover volumes não utilizados
docker volume prune

# Remover todos os volumes não utilizados (sem confirmação)
docker volume prune -f
```

### Limpeza Geral

```bash
# Limpar tudo (containers parados, redes, imagens não utilizadas)
docker system prune

# Limpar tudo incluindo volumes
docker system prune -a --volumes

# Ver espaço usado pelo Docker
docker system df
```

## PostgreSQL - Comandos Específicos

### Executar comandos SQL no container

```bash
# Conectar ao PostgreSQL
docker exec -it db_i_compras psql -U postgres

# Listar bancos de dados
docker exec db_i_compras psql -U postgres -c "\l"

# Listar tabelas de um banco
docker exec db_i_compras psql -U postgres -d icomprasclientes -c "\dt"

# Descrever estrutura de uma tabela
docker exec db_i_compras psql -U postgres -d icomprasclientes -c "\d clientes"

# Executar query SQL
docker exec db_i_compras psql -U postgres -d icomprasclientes -c "SELECT * FROM clientes;"
```

### Backup e Restore

```bash
# Fazer backup de um banco
docker exec db_i_compras pg_dump -U postgres icomprasclientes > backup_clientes.sql

# Restaurar backup
docker exec -i db_i_compras psql -U postgres icomprasclientes < backup_clientes.sql

# Backup de todos os bancos
docker exec db_i_compras pg_dumpall -U postgres > backup_all.sql
```

### Ver logs do PostgreSQL

```bash
# Ver logs do container
docker logs db_i_compras

# Ver logs em tempo real
docker logs -f db_i_compras

# Ver últimas 50 linhas
docker logs db_i_compras --tail 50
```

## Comandos PowerShell (Windows)

### Parar todos os containers
```powershell
docker ps -q | ForEach-Object { docker stop $_ }
```

### Remover todos os containers
```powershell
docker ps -aq | ForEach-Object { docker rm -f $_ }
```

### Remover todas as imagens
```powershell
docker images -q | ForEach-Object { docker rmi -f $_ }
```

### Remover diretório de dados (Windows)
```powershell
Remove-Item -Recurse -Force icompras-servicos/database/data
```

## Dicas Importantes

⚠️ **Atenção com comandos destrutivos:**
- Comandos com `-v` ou `--volumes` apagam dados permanentemente
- Comandos com `-f` ou `--force` não pedem confirmação
- Sempre faça backup antes de remover volumes

✅ **Boas práticas:**
- Use `docker compose down` para parar serviços (preserva volumes)
- Use `docker compose down -v` apenas quando quiser recriar do zero
- Monitore logs com `docker logs -f` para debug
- Use `docker system prune` periodicamente para liberar espaço

📝 **Persistência de dados:**
- Volumes Docker preservam dados entre reinicializações
- O diretório `./data` contém todos os dados do PostgreSQL
- Scripts em `/docker-entrypoint-initdb.d/` só executam na primeira inicialização
