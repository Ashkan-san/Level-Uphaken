import Head from 'next/head'
import { useState } from 'react'
import { getSession } from 'next-auth/client'

export default function ForgotPassword({}) {
  const [isProgressStarted, setProgressStarted] = useState(false)
  const [error, setError] = useState('')
  const [info, setInfo] = useState('')

  const handleForm = async (e) => {
    e.preventDefault()
    setInfo('')
    setError('')
    setProgressStarted(true)
    if (e.target.email.value.match(/^[a-zA-Z0-9_+-.]+@haw-hamburg\.de$/)) {
      await new Promise(resolve => setTimeout(resolve, 500));
      setInfo('E-Mail mit Wiederherstellungsoptionen erfolgreich versandt! (Dummy!)')
    } else {
      setError('Fehlerhafte Mail angegeben')
    }
    setProgressStarted(false)
  }

  return (
    <>
      <Head>
        <title>Passwort vergessen</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <h1 className="auth-title mb-4">Passwort vergessen</h1>
        </div>
        { error &&
          <section className="section">
            <div className="alert alert-danger">
                <h4 className="alert-heading">Fehler!</h4>
                <p>{error}</p>
            </div>
          </section>
        }
        { info &&
          <section className="section">
            <div className="alert alert-success">
                <h4 className="alert-heading">Erfolg!</h4>
                <p>{info}</p>
            </div>
          </section>
        }
        <section className="section">
          <form onSubmit={handleForm} className={error ? 'form-error' : ''}>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="email" type="email" className="form-control form-control-xl" placeholder="HAW Mailadresse" />
              <div className="form-control-icon">
                <i className="bi bi-envelope"></i>
              </div>
            </div>
            <button disabled={isProgressStarted} className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Register</button>
          </form>
          </section>
          <section className="section">
          <div className="text-center mt-5 text-lg fs-4">
            <p className="text-gray-600">Du hast bereits einen Account ? <a href="/auth/login" className="font-bold">Melde dich hier an</a></p>
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
    context.res.writeHead(302, { Location: '/user/dashboard' })
    context.res.end()
  }

  return {props: {}}
}
