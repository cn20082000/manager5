package model.entity

import jakarta.persistence.*
import library.cndatabase.CnEntity
import model.enums.RoleEnum
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_user")
open class UserEntity : CnEntity() {
    @Column(name = "_name", nullable = false)
    open var name: String? = null

    @Column(name = "birthday", nullable = false)
    open var birthday: LocalDateTime? = null

    @Column(name = "address")
    open var address: String? = null

    @Column(name = "phone_number")
    open var phoneNumber: String? = null

    @Column(name = "signature")
    open var signature: String? = null

    @Column(name = "username", nullable = false, unique = true, length = 32)
    open var username: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    open var role: RoleEnum? = null
}