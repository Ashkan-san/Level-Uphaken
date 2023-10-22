export async function requestAPI(url, session, options = {}) {
  
  if (!options.headers) {
    options.headers = {}
  }
  if (session.user) {
    options.headers["Authorization"] = "Bearer " + session.user.session
  }

  const res = await fetch(url, options)
  const data = await res.json()

  if (res.status >= 400) {
    const error = new Error(data.error)
    error.title = data.error
    error.status = data.status
    error.data = data.message
    error.timestamp = data.timestamp
    throw error
  }

  return data
}