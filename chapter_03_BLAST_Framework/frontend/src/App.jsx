import { useState } from 'react'

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://127.0.0.1:8000'

function parseStoryIds(input) {
  return [...new Set(
    input
      .split(',')
      .map((s) => s.trim())
      .filter(Boolean)
      .map(Number)
      .filter((n) => Number.isInteger(n) && n > 0)
  )]
}

export default function App() {
  const [storyIdsInput, setStoryIdsInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [testPlans, setTestPlans] = useState([])
  const [fetchErrors, setFetchErrors] = useState([])
  const [downloadUrl, setDownloadUrl] = useState(null)

  async function handleGenerate(e) {
    e.preventDefault()
    const storyIds = parseStoryIds(storyIdsInput)
    if (storyIds.length === 0) {
      setError('Enter at least one valid User Story ID.')
      return
    }

    setLoading(true)
    setError(null)
    setTestPlans([])
    setFetchErrors([])
    setDownloadUrl(null)

    try {
      const res = await fetch(`${API_BASE}/api/test-plan`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ story_ids: storyIds }),
      })
      const data = await res.json()
      if (!res.ok) {
        throw new Error(data.detail || `Request failed (${res.status})`)
      }
      setTestPlans(data.test_plans)
      setFetchErrors(data.errors || [])
      setDownloadUrl(data.download_url)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <header>
        <h1>ADO Test Plan Generator</h1>
        <p className="subtitle">
          Fetch User Stories from Azure DevOps and generate a structured,
          traceable test plan for each.
        </p>
      </header>

      <form className="story-form" onSubmit={handleGenerate}>
        <label htmlFor="story-ids">User Story ID(s)</label>
        <div className="story-form-row">
          <input
            id="story-ids"
            type="text"
            placeholder="e.g. 12345, 67890"
            value={storyIdsInput}
            onChange={(e) => setStoryIdsInput(e.target.value)}
          />
          <button type="submit" disabled={loading}>
            {loading ? 'Generating…' : 'Generate Test Plan'}
          </button>
        </div>
      </form>

      {error && <div className="banner banner-error">{error}</div>}

      {fetchErrors.length > 0 && (
        <div className="banner banner-warning">
          {fetchErrors.map((msg, i) => (
            <div key={i}>{msg}</div>
          ))}
        </div>
      )}

      {downloadUrl && (
        <a className="download-link" href={`${API_BASE}${downloadUrl}`}>
          Download Excel (.xlsx)
        </a>
      )}

      {testPlans.map((plan) => (
        <section className="story-card" key={plan.story_id}>
          <h2>
            #{plan.story_id} — {plan.story_title}
          </h2>

          {plan.flags?.length > 0 && (
            <div className="banner banner-warning">
              {plan.flags.map((f, i) => (
                <div key={i}>{f}</div>
              ))}
            </div>
          )}

          {plan.test_cases.length > 0 && (
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Type</th>
                  <th>Priority</th>
                  <th>Title</th>
                  <th>Expected Result</th>
                </tr>
              </thead>
              <tbody>
                {plan.test_cases.map((tc) => (
                  <tr key={tc.test_case_id}>
                    <td>{tc.test_case_id}</td>
                    <td>
                      <span className={`badge badge-${tc.type}`}>{tc.type}</span>
                    </td>
                    <td>{tc.priority}</td>
                    <td>{tc.title}</td>
                    <td>{tc.expected_result}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>
      ))}
    </div>
  )
}
