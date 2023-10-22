import Image from 'next/image'
import Link from 'next/link'
import getConfig from 'next/config'
import swal from 'sweetalert'
import { useState } from 'react'
import { useRouter } from 'next/router'
import { requestAPI } from '../utils/request'

export default function SubjectCard({ session, subject, member }) {
  
  const router = useRouter()
  const { publicRuntimeConfig } = getConfig()
  const [key, setKey] = useState('')
  const [keyValid, setKeyValid] = useState(-1)

  const keyValidRegExp = /^(?=.*\d)(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,16}$/
  const updateKey = (value) => {
    setKey(value)
    if (value.match(keyValidRegExp)) {
      setKeyValid(1)
    } else {
      setKeyValid(0)
    }
  }

  const handleJoin = async (e) => {
    e.preventDefault()

    try {
      await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/users/enroll/${subject.acronym}/${e.target.key.value}`, session, { method: 'PUT' })
      router.push(`/subject/${subject.id}`)
    } catch (error) {
      swal({
        title: 'Falscher Schlüssel!',
        icon: 'error',
      })
    }    
  }

  return (
    <div className="card col-xl-2 col-lg-4 col-sm-10 m-4 text-center">
      <div className="card-header">
        <h4 className="card-title">{subject.subjectName}</h4>
      </div>
      <div className="card-body">
        <Image src={`https://eu.ui-avatars.com/api/?name=${subject.acronym}&size=512`} width='256px' height='256px' />
        {(member && (
          <h2 className="p-3">
            <Link href={`/subject/${subject.id}`}>
              <a>
                Zum Kurs
              </a>
            </Link>
          </h2>
        )) || (session.user.role !== 'PROFESSOR' && (
          <form onSubmit={handleJoin}>
            <div className="form-group mt-3">
              <label className="sr-only">
                <input type="password" className={`form-control ${keyValid === -1 ? '' : keyValid === 1 ? 'is-valid' : 'is-invalid'}`} name="key" placeholder="Einschreibeschl&uuml;ssel" value={key} onChange={(e) => updateKey(e.target.value)} required />
                {keyValid === 0 && (
                  <div className="invalid-feedback">
                    <i className="bx bx-radio-circle"></i> Einschreibeschl&uuml;ssel ist nicht valide!
                  </div>
                )}
              </label>
            </div>
          </form>
        ))}
      </div>
    </div>
  )
}