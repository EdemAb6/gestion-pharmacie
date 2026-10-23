# Pharmacy Management

Console-based Java application for managing pharmacy operations with MySQL and
JDBC.

## Database Configuration

The application reads MySQL credentials from environment variables, so the
database password is not stored in the source code.

In PowerShell, set the variables before starting the application:

```powershell
$env:PHARMACIE_DB_PASSWORD = "your-mysql-password"
$env:PHARMACIE_DB_USER = "root"
$env:PHARMACIE_DB_URL = "jdbc:mysql://localhost:3306/pharmacie_db?serverTimezone=UTC"
```

`PHARMACIE_DB_USER` and `PHARMACIE_DB_URL` are optional and default to `root`
and the local URL shown above. `PHARMACIE_DB_PASSWORD` is required.
