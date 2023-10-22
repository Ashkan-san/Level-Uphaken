import Head from 'next/head'
import { useState, useEffect } from 'react'
import { useRouter } from 'next/router'
import { signIn, getSession } from 'next-auth/client'

export default function LogIn() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [isProgressStarted, setProgressStarted] = useState(false)
  const [error, setError] = useState('')
  const router = useRouter()

  useEffect(() => {
    if (router.query.error) {
      setError(router.query.error)
      setProgressStarted(false)
      if (router.query.email) {
        setEmail(router.query.email)
      }
    }
  }, [router])

  const handleLogin = (e) => {
    e.preventDefault()
    setProgressStarted(true)
    signIn('credentials', {
      email,
      password,
      callbackUrl: `${window.location.origin}/user/dashboard`
    })
  }

  return (
    <>
      <Head>
        <title>Anmeldung</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <h1 className="auth-title mb-4">Anmeldung</h1>
        </div>
        { error &&
          <section className="section">
            <div className="alert alert-danger">
                <h4 className="alert-heading">Fehler!</h4>
                <p>{error}</p>
            </div>
          </section>
        }
        <section className="section">
          <form onSubmit={(e) => handleLogin(e)} className={error ? 'form-error' : ''}>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="email" type="email" className="form-control form-control-xl" placeholder="HAW Mailadresse" value={email} onChange={(e) => setEmail(e.target.value)}/>
              <div className="form-control-icon">
                <i className="bi bi-envelope"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="password" type="password" className="form-control form-control-xl" placeholder="Passwort" value={password} onChange={(e) => setPassword(e.target.value)} />
              <div className="form-control-icon">
                <i className="bi bi-shield-lock"></i>
              </div>
            </div>
            <button disabled={isProgressStarted} className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Anmelden</button>
          </form>
          </section>
          <section className="section">
          <div className="text-center mt-5 text-lg fs-4">
            <p className="text-gray-600">Du hast noch keinen Account? <a href="/auth/register" className="font-bold">Registriere dich hier</a></p>
            <p><a className="font-bold" href="/auth/forgot-password">Passwort vergessen ?</a></p>
          </div>
        </section>
      </div>
    </>
  )
}

export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (session) {
    context.res.writeHead(302, { Location: '/auth/logout' })
    context.res.end()
  }

  return {props: {}}
}
