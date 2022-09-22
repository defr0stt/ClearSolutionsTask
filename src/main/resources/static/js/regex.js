function checkForm(){
    let email = document.getElementById("email").value;
    let fName = document.getElementById("first_name").value;
    let lName = document.getElementById("last_name").value;
    let birthDate = document.getElementById("birth_date").value;
    let address = {
        address_street : document.getElementById("street").value,
        address_building : document.getElementById("building_number").value,
        address_flat : document.getElementById("flat_number").value
    };
    let phone = document.getElementById("phone_number").value;
    let invalidResult = 'Invalid data in the next fields : ';
    let checkTemp = invalidResult.length;

    // validation section
    if(!email.match(/[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,}/))
        invalidResult += 'Email address; '
    if(!fName.match(/(^[a-zA-Z][a-zA-Z\s]{0,50}[a-zA-Z]$)/))
        invalidResult += 'First name; '
    if(!lName.match(/(^[a-zA-Z][a-zA-Z\s]{0,50}[a-zA-Z]$)/))
        invalidResult += 'Last name; '
    if(!birthDate.match(/([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/))
        invalidResult += 'Birth date; '
    else {
        if(new Date(Date.now()).getFullYear() - new Date(birthDate).getFullYear() < 18){
            document.getElementById("invalid_data").innerHTML = "Birth data is incorrect";
            document.getElementById("invalid_data").style.display = 'block';
            return false;
        }
    }

    if(address.address_street.length != 0)
        if(!address.address_street.match(/(^[a-zA-Z][a-zA-Z\s]{0,50}[a-zA-Z]$)/))
            invalidResult += 'Street name; '
    if(address.address_building.length != 0)
        if(!address.address_building.match(/^((?!(0))[0-9]{1,})$/))     // not zero values
            invalidResult += 'Building number; '
    if(address.address_flat.length != 0)
        if(!address.address_flat.match(/^((?!(0))[0-9]{1,})$/))     // not zero values
            invalidResult += 'Flat number; '
    if(phone.length != 0)
        if(!phone.match(/[0-9]{9}/))
            invalidResult += 'Phone number; '
    
    // result
    if(invalidResult.length != checkTemp){
        document.getElementById("invalid_data").innerHTML = invalidResult;
        document.getElementById("invalid_data").style.display = 'block';
        return false;
    }
    return true;
}