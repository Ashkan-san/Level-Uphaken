import Head from 'next/head'
import getConfig from 'next/config'
import { useState } from 'react'
import { signIn, getSession, session } from 'next-auth/client'
import { requestAPI } from '../../utils/request'

export default function Register() {
  const { publicRuntimeConfig } = getConfig()

  const [isProgressStarted, setProgressStarted] = useState(false)
  const [error, setError] = useState('')

  async function handleRegister(event) {
    event.preventDefault()
    setProgressStarted(true)
    try {
      await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/users`, {}, {
        body: JSON.stringify({
          emailAddress: event.target.email.value,
          firstName: event.target.firstName.value,
          lastName: event.target.lastName.value,
          aKennung: event.target.aKennung.value,
          password: event.target.password.value,
          academicCourse: event.target.academicCourse.value,
        }),
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'POST'
      })
      signIn('credentials', {
        email: event.target.email.value,
        password: event.target.password.value,
        callbackUrl: `${window.location.origin}/user/dashboard`
      })
    } catch (error) {
      console.error(error);
      setError(error.data ? error.data : 'Fehler beim abschicken!')
      setProgressStarted(false)
    }
  }

  return (
    <>
      <Head>
        <title>Registrierung</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <h1 className="auth-title mb-4">Registrierung</h1>
        </div>
        {error &&
          <section className="section">
            <div className="alert alert-danger">
              <h4 className="alert-heading">Fehler!</h4>
              <p>{error}</p>
            </div>
          </section>
        }
        <section className="section">
          <form onSubmit={handleRegister} className={error ? 'form-error' : ''}>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="email" type="email" className="form-control form-control-xl" placeholder="HAW Mailadresse" />
              <div className="form-control-icon">
                <i className="bi bi-envelope"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="firstName" type="text" className="form-control form-control-xl" placeholder="Vorname" />
              <div className="form-control-icon">
                <i className="bi bi-person"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="lastName" type="text" className="form-control form-control-xl" placeholder="Nachname" />
              <div className="form-control-icon">
                <i className="bi bi-person"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="aKennung" type="text" className="form-control form-control-xl" placeholder="AKennung" />
              <div className="form-control-icon">
                <i className="bi bi-person-badge"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="academicCourse" type="text" className="form-control form-control-xl" placeholder="Studiengang" />
              <div className="form-control-icon">
                <i className="bi bi-vector-pen"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="password" type="password" className="form-control form-control-xl" placeholder="Passwort" />
              <div className="form-control-icon">
                <i className="bi bi-shield-lock"></i>
              </div>
            </div>
            <div className="form-group position-relative has-icon-left mb-4">
              <input name="password-confirmation" type="password" className="form-control form-control-xl" placeholder="Passwort bestätigen" />
              <div className="form-control-icon">
                <i className="bi bi-shield-lock"></i>
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

  return { props: {} }
}

