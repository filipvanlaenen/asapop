# Running ASAPOP for Romania Opinion Poll Analysis

## Quick Start Guide

### 1. Build the Project

```bash
cd /home/user/asapop
mvn clean compile assembly:single
```

### 2. Run Analysis on Example Romania Data

```bash
java -jar target/asapop-1.0-SNAPSHOT-jar-with-dependencies.jar \
  analyze \
  romania-example.ropf \
  romania-election-data.yaml \
  romania-results.yaml
```

### 3. View Results

```bash
cat romania-results.yaml
```

## Understanding the Analysis

### What the Analysis Does

1. **Finds Most Recent Polls**: For each polling firm (CURS, INSCOP, Avangarde), gets the latest poll
2. **Runs Statistical Simulations**: Uses hypergeometric distributions with:
   - 10,000 samples per distribution
   - 2,000,000 multivariate simulations
3. **Generates Confidence Intervals**: Provides probability ranges for each party

### The "Averages" You Get

The analysis doesn't provide simple averages but **statistical distributions**:

- **Median**: The most likely vote share (this is your "average")
- **95% Confidence Interval**: The range where the true value likely falls
- **Probability Mass Function**: Full distribution of possibilities

## Example Input (romania-example.ropf)

```
•PF: CURS •FS: 2024-01-10 •FE: 2024-01-15 •PD: 2024-01-20 •SC: N •SS: 1000 PSD:30 PNL:22 AUR:18 USR:15 UDMR:5 •O:10
•PF: INSCOP •FS: 2024-01-12 •FE: 2024-01-18 •PD: 2024-01-22 •SC: N •SS: 1200 PSD:32 PNL:20 AUR:17 USR:16 UDMR:6 •O:9
•PF: Avangarde •FS: 2024-01-15 •FE: 2024-01-20 •PD: 2024-01-25 •SC: N •SS: 900 PSD:31 PNL:21 AUR:19 USR:14 UDMR:5 •O:10
```

## Field Explanations

- `•PF`: Polling Firm name
- `•FS`: Fieldwork Start date (YYYY-MM-DD)
- `•FE`: Fieldwork End date (YYYY-MM-DD)
- `•PD`: Publication Date
- `•SC: N`: Scope = National elections
- `•SS`: Sample Size (number of people polled)
- `PSD:30`: Party abbreviation : vote percentage
- `•O:10`: Other parties (10%)

## Converting to CSV Format

If you want simple CSV output instead:

```bash
java -jar target/asapop-1.0-SNAPSHOT-jar-with-dependencies.jar \
  convert \
  romania-example.ropf \
  romania.csv \
  PSD PNL AUR USR UDMR
```

This creates a CSV file with:
- Poll dates
- Polling firms
- Results for each party
- Sample sizes

## Key Code Files

### Analysis Engine
- **File**: `src/main/java/net/filipvanlaenen/asapop/analysis/AnalysisEngine.java`
- **What it does**: Core statistical analysis
- **Key method**: `run()` at line 171

### Most Recent Polls Selection
- **Method**: `calculateMostRecentPolls()` at line 89
- **Logic**: Groups by polling firm, selects latest by fieldwork end date

### Vote Share Calculation
- **Method**: `calculateVoteSharesAnalysis()` at line 117
- **Logic**:
  1. Gets result percentage (e.g., 30%)
  2. Multiplies by effective sample size (30% × 1000 = 300)
  3. Generates hypergeometric distribution

### Statistical Method
- **File**: `src/main/java/net/filipvanlaenen/asapop/analysis/SampledHypergeometricDistributions.java`
- **What it does**: Creates probability distributions accounting for sampling uncertainty

## Advanced: Adding Real Romania Polls

To add real opinion poll data:

1. Create a proper ROPF file with actual poll data
2. Update party IDs in electoral lists section
3. Set correct population size in YAML (Romania voting population ≈ 18 million)

### Electoral List Format

```
PSD: RO001 •A: PSD •EN: Social Democratic Party •RO: Partidul Social Democrat
PNL: RO002 •A: PNL •EN: National Liberal Party •RO: Partidul Național Liberal
```

Format: `KEY: ID •A: Abbreviation •EN: English Name •RO: Romanian Name`

## Troubleshooting

### Maven Build Fails
- Ensure internet connection for dependency download
- Try: `mvn clean install -U` to force updates

### Java Version Error
- Requires Java 17+
- Check version: `java -version`

### Invalid ROPF Format
- Ensure bullet points (`•`) are used correctly
- Check date formats: YYYY-MM-DD
- Verify percentages sum to 100 (with •O for other)

## Understanding the Results

The output YAML contains statistical measures:

```yaml
responseScenarios:
  - responseScenario:
      # Poll metadata
    voteSharesAnalysis:
      electoralLists:
        - id: RO001  # PSD
          median: 31.0
          confidence95Lower: 28.2
          confidence95Upper: 33.8
```

### Interpretation

- **Median (31.0)**: Best estimate of PSD support
- **95% CI [28.2-33.8]**: PSD support is very likely between 28.2% and 33.8%
- The analysis accounts for sample size and statistical uncertainty

## Cache Directory

The analysis creates a cache at `~/.asapop/` to store:
- Sampled hypergeometric distributions
- This speeds up repeated analyses
- Safe to delete if needed

## Further Reading

- **README.md**: Complete format specification
- **ROADMAP.md**: Future features
- Original repository: https://github.com/filipvanlaenen/asapop
