import Head from 'next/head'
import getConfig from 'next/config'
import { getSession } from 'next-auth/client'
import { useState, useEffect } from 'react'
import { requestAPI } from '../../utils/request'

export default function Settings({ session, profile }) {

  function getProfileTab() {

    const [isProfileButtonEnabled, setProfileButtonEnabled] = useState(true)

    const [firstName, setFirstName] = useState(profile.firstName)
    const [firstNameValid, setFirstNameValid] = useState(true)
  
    const updateFirstName = (value) => {
      setFirstName(value)
      setFirstNameValid(value.length > 0)
    }

    const [lastName, setLastName] = useState(profile.lastName)
    const [lastNameValid, setLastNameValid] = useState(true)
  
    const updateLastName = (value) => {
      setLastName(value)
      setLastNameValid(value.length > 0)
    }

    const [emailAddress, setEmailAddress] = useState(profile.emailAddress)
    const [emailAddressValid, setEmailAddressValid] = useState(true)
  
    const emailAddressRegExp = /^[a-zA-Z0-9_+-.]+@haw-hamburg\.de$/
    const updateEmailAddress = (value) => {
      setEmailAddress(value)
      setEmailAddressValid(value.match(emailAddressRegExp))
    }
  
    const [aKennung, setAKennung] = useState(profile.aKennung)
    const [aKennungValid, setAKennungValid] = useState(true)
  
    const aKennungRegExp = /^a\w{2}\d{3}$/
    const updateAKennung = (value) => {
      setAKennung(value)
      setAKennungValid(value.match(aKennungRegExp))
    }

    const validValues = [firstNameValid, lastNameValid, emailAddressValid, aKennungValid]
    useEffect(() => {
      setProfileButtonEnabled(validValues.every(Boolean))
    }, validValues)

    const { publicRuntimeConfig } = getConfig()

    const handleProfle = async (e) => {
      e.preventDefault()
      setProfileButtonEnabled(false)
      profile = await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/users`, session, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          emailAddress,
          firstName,
          lastName,
          aKennung,
        })
      })
      setProfileButtonEnabled(true)
    }

    return (
      <form onSubmit={handleProfle}>
        <div className="row">
          <div className="col-12 mb-2">
            <p>
              Pass hier deine Kontoeinstellungen an.
            </p>
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="firstName">Vorname</label>
            <input type="text" className={`form-control ${firstNameValid ? "is-valid" : "is-invalid"}`} id="firstName" placeholder="Vorname"  value={firstName} onChange={(e) => updateFirstName(e.target.value)} required />
            {!firstNameValid && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Vorname darf nicht leer sein!
              </div>
            )}
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="lastName">Nachname</label>
            <input type="text" className={`form-control ${lastNameValid ? "is-valid" : "is-invalid"}`} id="lastName" placeholder="Nachname"  value={lastName} onChange={(e) => updateLastName(e.target.value)} required />
            {!lastNameValid && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Nachname darf nicht leer sein!
              </div>
            )}
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="emailAddress">E-Mail Adresse</label>
            <input type="text" className={`form-control ${emailAddressValid ? "is-valid" : "is-invalid"}`} id="emailAddress" placeholder="EMail Adresse" value={emailAddress} onChange={(e) => updateEmailAddress(e.target.value)} required />
            {!emailAddressValid && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Keine valide @haw-hamburg.de Adresse!
              </div>
            )}
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="aKennung">AKennung</label>
            <input type="text" className={`form-control ${aKennungValid ? "is-valid" : "is-invalid"}`} id="aKennung" placeholder="AKennung" value={aKennung} onChange={(e) => updateAKennung(e.target.value)} required />
            {!aKennungValid && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Keine valide AKennung!
              </div>
            )}
          </div>
          <div className="col-6 offset-3 mb-2">
            <button disabled={!isProfileButtonEnabled} className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Speichern</button>
          </div>
        </div>
      </form>
    )
  }

  function getPasswordTab() {

    const [isPasswordButtonEnabled, setPasswordButtonEnabled] = useState(true)

    const [newPassword, setNewPassword] = useState('')
    const [newPasswordValid, setNewPasswordValid] = useState(-1)

    const newPasswordRegExp = /^(?=.*\d)(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,16}$/
    const updateNewPassword = (value) => {
      setNewPassword(value)
      if (value.match(newPasswordRegExp)) {
        setNewPasswordValid(1)
      } else {
        setNewPasswordValid(0)
      }
    }

    const [newPasswordConfirmation, setNewPasswordConfirmation] = useState('')
    const [newPasswordConfirmationValid, setNewPasswordConfirmationValid] = useState(-1)

    const updateNewPasswordConfirmation = (value) => {
      setNewPasswordConfirmation(value)
      setNewPassword(value === newPassword)
      if (value.match(newPasswordRegExp)) {
        setNewPasswordConfirmationValid(1)
      } else {
        setNewPasswordConfirmationValid(0)
      }
    }

    const validValues = [newPasswordValid, newPasswordConfirmationValid]
    useEffect(() => {
      setPasswordButtonEnabled(validValues.every(elm => elm === 1))
    }, validValues)

    const { publicRuntimeConfig } = getConfig()

    const handlePassword = async (e) => {
      e.preventDefault()
      setPasswordButtonEnabled(false)
      await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/users`, session, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ password: newPassword })
      })
      setPasswordButtonEnabled(true)
    }

    return (
      <form onSubmit={handlePassword}>
        <div className="row">
          <div className="col-12 mb-2">
            <p>
              Passe hier dein Passwort an.
            </p>
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="newPassword">Neues Passwort</label>
            <input type="password" className={`form-control ${newPasswordValid === -1 ? '' : newPasswordValid === 1 ? 'is-valid' : 'is-invalid'}`} id="newPassword" placeholder="*********"  value={newPassword} onChange={(e) => updateNewPassword(e.target.value)} required />
            {newPasswordValid === 0 && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Neues Passwort entspricht nicht den Vorgaben!
              </div>
            )}
          </div>
          <div className="col-12 mb-2">
            <label htmlFor="newPasswordConfirmation">Neues Passwort bestätigen</label>
            <input type="password" className={`form-control ${newPasswordConfirmationValid === -1 ? '' : newPasswordConfirmationValid === 1 ? 'is-valid' : 'is-invalid'}`} id="newPasswordConfirmation" placeholder="*********"  value={newPasswordConfirmation} onChange={(e) => updateNewPasswordConfirmation(e.target.value)} required />
            {newPasswordConfirmationValid === 0 && (
              <div className="invalid-feedback">
                <i className="bx bx-radio-circle"></i> Passwörter stimmen nicht überein!
              </div>
            )}
          </div>
          <div className="col-6 offset-3 mb-2">
            <button disabled={!isPasswordButtonEnabled} className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Speichern</button>
          </div>
        </div>
      </form>
    )
  }


  return (
    <>
      <Head>
        <title>Einstellungen</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
            <div className="row">
                <div className="col mb-3">
                    <h3>Einstellungen</h3>
                </div>
            </div>
        </div>
        <section className="section row">
          <div className="col">
            <div className="card" style={{ minHeight: '75vh' }}>
              <div className="card-body">
                <div className="row">
                  <div className="col-2">
                    <div className="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                      <a className="nav-link active" id="v-pills-general-tab" data-bs-toggle="pill" href="#v-pills-general" role="tab" aria-controls="v-pills-general" aria-selected="true"><i className="icon-mid bi bi-gear me-2"></i> Allgemein</a>
                      <a className="nav-link" id="v-pills-profile-tab" data-bs-toggle="pill" href="#v-pills-profile" role="tab" aria-controls="v-pills-profile" aria-selected="false"><i className="icon-mid bi bi-person-fill me-2"></i>  Konto</a>
                      <a className="nav-link" id="v-pills-password-tab" data-bs-toggle="pill" href="#v-pills-password" role="tab" aria-controls="v-pills-password" aria-selected="false"><i className="icon-mid bi bi-shield-lock-fill me-2"></i> Passwort</a>
                      <a className="nav-link" id="v-pills-theme-tab" data-bs-toggle="pill" href="#v-pills-theme" role="tab" aria-controls="v-pills-theme" aria-selected="false"><i className="icon-mid bi bi-palette-fill me-2"></i> Thema</a>
                      <a className="nav-link" id="v-pills-notifications-tab" data-bs-toggle="pill" href="#v-pills-notifications" role="tab" aria-controls="v-pills-notifications" aria-selected="false"><i className="icon-mid bi bi-bell-fill me-2"></i> Settings</a>
                    </div>
                  </div>
                  <div className="col-10">
                    <div className="tab-content" id="v-pills-tabContent">
                      <div className="tab-pane fade active show" id="v-pills-general" role="tabpanel" aria-labelledby="v-pills-general-tab">
                        Passe hier deine allgemeinen Einstellungen an.
                      </div>
                      <div className="tab-pane fade" id="v-pills-profile" role="tabpanel" aria-labelledby="v-pills-profile-tab">
                        {getProfileTab()}
                      </div>
                      <div className="tab-pane fade" id="v-pills-password" role="tabpanel" aria-labelledby="v-pills-password-tab">
                        {getPasswordTab()}
                      </div>
                      <div className="tab-pane fade" id="v-pills-theme" role="tabpanel" aria-labelledby="v-pills-theme-tab">
                        Passe hier dein individuelles Thema an.
                      </div>
                      <div className="tab-pane fade" id="v-pills-notifications" role="tabpanel" aria-labelledby="v-pills-notifications-tab">
                        Passe hier deine Benachrichtigungen an.
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </>
  )
}

export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (!session) {
    context.res.writeHead(302, { Location: '/auth/login' })
    context.res.end()
    return {props: {}}
  }

  try {
    const profile = await requestAPI(`${process.env.API_ENDPOINT}/users/info`, session, context)
    return {
      props: { session, profile }
    }
  } catch (error) {
    context.res.writeHead(302, { Location: '/auth/logout' })
    context.res.end()
    return {props: {}}
  }
}