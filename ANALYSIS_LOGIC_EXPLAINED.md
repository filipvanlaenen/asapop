# ASAPOP Analysis Logic - Deep Dive

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         INPUT FILES                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  romania-example.ropf          romania-election-data.yaml       │
│  ┌──────────────────┐           ┌──────────────────┐           │
│  │ •PF: CURS        │           │ populationSize:   │           │
│  │ •SS: 1000        │           │   18_000_000      │           │
│  │ PSD:30 PNL:22    │           └──────────────────┘           │
│  │                  │                                            │
│  │ •PF: INSCOP      │                                            │
│  │ •SS: 1200        │                                            │
│  │ PSD:32 PNL:20    │                                            │
│  └──────────────────┘                                            │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│                    PARSER (RichOpinionPollsFile)                │
├─────────────────────────────────────────────────────────────────┤
│  Reads ROPF format and creates:                                 │
│  - OpinionPoll objects                                           │
│  - ElectoralList objects                                         │
│  - ResponseScenario objects                                      │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│              ANALYSIS ENGINE - STEP 1                            │
│              calculateMostRecentPolls()                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Groups polls by polling firm:                                  │
│                                                                   │
│  CURS      → Poll from 2024-01-15 ✓                             │
│  INSCOP    → Poll from 2024-01-18 ✓                             │
│  Avangarde → Poll from 2024-01-20 ✓                             │
│                                                                   │
│  Result: 3 most recent polls (one per firm)                     │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│              ANALYSIS ENGINE - STEP 2                            │
│              calculateVoteSharesAnalysis()                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  For EACH poll, for EACH party:                                 │
│                                                                   │
│  Example: CURS poll, PSD result                                 │
│    • Result: 30%                                                 │
│    • Sample size: 1000                                           │
│    • Responses: 30% × 1000 = 300 people                         │
│                                                                   │
│  Create Hypergeometric Distribution:                            │
│    • Population: 18,000,000 (Romania voters)                    │
│    • Sample: 1000                                                │
│    • Successes in sample: 300                                   │
│    • Generate 10,000 simulations                                │
│                                                                   │
│  Result: Probability distribution for PSD support               │
│                                                                   │
│  Repeat for: PNL, AUR, USR, UDMR                                │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│              STATISTICAL SIMULATIONS                             │
│              SampledHypergeometricDistributions                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  For PSD (30% with n=1000):                                     │
│                                                                   │
│  Simulation 1:    29.8%                                          │
│  Simulation 2:    30.2%                                          │
│  Simulation 3:    29.5%                                          │
│  ...                                                              │
│  Simulation 10000: 30.3%                                         │
│                                                                   │
│  Creates probability distribution:                              │
│                                                                   │
│       Probability                                                │
│          ▲                                                       │
│          │       ▄▄▄                                             │
│          │     ▄█████▄                                           │
│          │   ▄███████████▄                                       │
│          │ ▄███████████████▄                                     │
│          └─────────────────────→ Vote Share (%)                 │
│              28  29  30  31  32                                  │
│                                                                   │
│  Generates:                                                      │
│    • Median: 30.0%                                               │
│    • 95% CI: [28.1%, 31.9%]                                     │
│    • Full probability mass function                             │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│              MULTIVARIATE ANALYSIS                               │
│              (if presidential election)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  For presidential first round:                                  │
│    • Run 2,000,000 multivariate simulations                     │
│    • Calculate who wins first round                             │
│    • Generate win probabilities                                  │
│                                                                   │
│  (Not used for parliamentary elections)                         │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│                    OUTPUT - romania-results.yaml                │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  responseScenarios:                                              │
│    - metadata:                                                   │
│        pollingFirm: "CURS"                                       │
│        sampleSize: 1000                                          │
│      voteShares:                                                 │
│        PSD:                                                       │
│          median: 30.0                                            │
│          lowerBound95: 28.1                                      │
│          upperBound95: 31.9                                      │
│        PNL:                                                       │
│          median: 22.0                                            │
│          lowerBound95: 20.3                                      │
│          upperBound95: 23.7                                      │
│        ...                                                        │
└─────────────────────────────────────────────────────────────────┘
```

## Key Algorithms Explained

### 1. Most Recent Polls Selection (AnalysisEngine.java:89-108)

```java
// Pseudocode
Map<PollingFirm, OpinionPoll> mostRecent = {}

for each poll in allPolls:
    firm = poll.getPollingFirm()

    if firm not in mostRecent:
        mostRecent[firm] = poll
    else:
        if poll.fieldworkEnd > mostRecent[firm].fieldworkEnd:
            mostRecent[firm] = poll
        else if poll.fieldworkEnd == mostRecent[firm].fieldworkEnd:
            // Keep both if same date
            mostRecent.add(firm, poll)

return mostRecent.values()
```

**Why?** Avoids double-counting polls from the same firm. Only uses their most recent data.

### 2. Vote Share Analysis (AnalysisEngine.java:117-128)

```java
// Pseudocode
for each electoralList in poll:
    result = poll.getResult(electoralList)  // e.g., 30.0
    sampleSize = poll.getEffectiveSampleSize()  // e.g., 1000

    // Convert percentage to number of respondents
    responses = round(result * sampleSize / 100)  // 30 * 1000 / 100 = 300

    // Generate hypergeometric distribution
    distribution = SampledHypergeometric(
        successes: responses,           // 300 people said PSD
        sampleSize: sampleSize,         // 1000 total polled
        populationSize: 18_000_000,     // Romania voters
        numberOfSamples: 10_000         // Simulations
    )

    voteSharesAnalysis.add(electoralList, distribution)
```

### 3. Hypergeometric Distribution (Why not simple average?)

**Simple Average Problems:**
```
Poll 1 (n=1000): PSD = 30%
Poll 2 (n=1200): PSD = 32%
Poll 3 (n=900):  PSD = 31%

Simple average: (30 + 32 + 31) / 3 = 31%
```

**Problems with this:**
- Ignores sample sizes (1200 sample more reliable than 900)
- No confidence intervals
- No uncertainty quantification

**Hypergeometric Distribution:**
- Models sampling without replacement
- Accounts for finite population
- Provides full probability distribution
- Larger samples → narrower confidence intervals

**Example:**
```
Poll with n=1000, result=30%:
  → 95% CI: [28.1%, 31.9%]  (±1.9%)

Poll with n=100, result=30%:
  → 95% CI: [22.0%, 38.0%]  (±8.0%)
```

### 4. Why Sample from Distribution? (10,000 iterations)

Direct calculation of confidence intervals is complex for multivariate scenarios.

**Sampling approach:**
1. Generate 10,000 random draws from the hypergeometric distribution
2. Sort the results
3. 95% CI = [250th value, 9750th value]
4. Median = 5000th value

**Benefits:**
- Simple to implement
- Works for complex scenarios
- Can combine multiple distributions
- Cache results for reuse

### 5. File Cache (~/.asapop/)

**Why cache?**

Generating 10,000 samples is expensive:
- Hypergeometric calculations are complex
- Same distributions used repeatedly
- Cache speeds up by ~100x

**Cache structure:**
```
~/.asapop/sampled-hypergeometric-distributions/
  18000000/           ← population size
    1000/             ← sample size
      300.yaml        ← number of successes
      220.yaml
      ...
```

**When you run analysis multiple times with similar parameters, it reuses cached distributions.**

## Comparison: Simple Average vs. ASAPOP

### Scenario: 3 Romania polls

| Firm      | Sample | PSD  |
|-----------|--------|------|
| CURS      | 1000   | 30%  |
| INSCOP    | 1200   | 32%  |
| Avangarde | 900    | 31%  |

### Simple Average Method
```
Average = (30 + 32 + 31) / 3 = 31.0%
Result: "PSD has 31% support"
```

### ASAPOP Method
```
For CURS (n=1000, result=30%):
  → Distribution with median=30.0, CI=[28.1, 31.9]

For INSCOP (n=1200, result=32%):
  → Distribution with median=32.0, CI=[30.3, 33.7]

For Avangarde (n=900, result=31%):
  → Distribution with median=31.0, CI=[29.0, 33.0]

Analysis output per firm:
- CURS: PSD median 30.0% (95% CI: 28.1-31.9%)
- INSCOP: PSD median 32.0% (95% CI: 30.3-33.7%)
- Avangarde: PSD median 31.0% (95% CI: 29.0-33.0%)
```

**Result:** "Based on most recent polls, PSD support ranges from 30-32% across different firms, with uncertainty ranges accounting for sample sizes."

## What "Averages" You Get

ASAPOP doesn't provide a single combined average. Instead, for each polling firm's most recent poll, you get:

1. **Median** - The most likely value (similar to average)
2. **95% Confidence Interval** - Range of plausible values
3. **Full distribution** - Complete probability curve

**Why per-firm?** Different firms may have different methodologies, house effects, etc. Better to see them separately than combine into one potentially misleading number.

## Running Analysis Commands

### Basic Analysis
```bash
java -jar asapop.jar analyze \
  input.ropf \          # Opinion polls
  election-data.yaml \  # Population size
  results.yaml          # Output file
```

### Convert to CSV (simpler format)
```bash
java -jar asapop.jar convert \
  input.ropf \
  output.csv \
  PSD PNL AUR USR UDMR  # Parties to include
```

### Filter by Area (for regional analysis)
```bash
java -jar asapop.jar convert \
  input.ropf \
  output.csv \
  PSD PNL AUR \
  -a=TR  # Only Transilvania region polls
```

## Performance Notes

- **First run**: Slow (generates distributions, populates cache)
- **Subsequent runs**: Fast (reuses cached distributions)
- **Memory**: Requires ~2GB RAM for large datasets
- **Time**: ~30 seconds for typical dataset (after cache populated)

## Advanced: Understanding Effective Sample Size

The code uses "effective sample size" not just stated sample size.

**Why?**
```
Poll reports:
  • 1250 people contacted
  • 20% refused to answer
  • Of 1000 that answered: PSD:30%, PNL:20%, AUR:15%, USR:10%

Effective sample size = 1250 - 20% = 1000
```

The effective sample size accounts for:
- Non-responses
- Excluded responses
- Only counts people who gave valid answers

**Formula:** `effectiveSampleSize = sampleSize × (1 - excluded/100)`

## Code Locations Reference

| Feature | File | Line |
|---------|------|------|
| Main entry point | CommandLineInterface.java | 72 |
| Analysis command | CommandLineInterface.java | 114-130 |
| Most recent polls | AnalysisEngine.java | 89-108 |
| Vote share calculation | AnalysisEngine.java | 117-128 |
| Run analysis | AnalysisEngine.java | 190-203 |
| Hypergeometric distribution | SampledHypergeometricDistributions.java | - |
| File cache | SampledHypergeometricDistributionsFileCache.java | - |
| ROPF parser | RichOpinionPollsFile.java | - |

## Common Pitfalls

1. **Missing population size** → Analysis fails
2. **Invalid ROPF format** → Parser errors
3. **No recent polls** → Empty results
4. **Internet required** → For Maven build only (not for running)
5. **Cache grows large** → Safe to delete ~/.asapop/ if needed
